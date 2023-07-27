package de.uniks.beastopia.teaml.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.util.Pair;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;


@Module
public class HttpModule {
    public record EndpointRateLimit(
            String endpoint,
            int maxRequests,
            int timeFrameSeconds
    ) {
    }

    private static final List<EndpointRateLimit> ENDPOINT_RATE_LIMITS = List.of(
            new EndpointRateLimit("auth/.*", 10, 11),
            new EndpointRateLimit("achievements.*", 10, 11),
            new EndpointRateLimit("monsters/.*/image", 210, 61),
            new EndpointRateLimit("items/.*/image", 60, 61),
            new EndpointRateLimit("characters/.*\\.png", 90, 61),
            new EndpointRateLimit("tilesets/.*", 10, 11),
            new EndpointRateLimit("characters.*", 90, 61),
            new EndpointRateLimit("users.*", 10, 11),
            new EndpointRateLimit("groups.*", 10, 11),
            new EndpointRateLimit("messages.*", 10, 11),
            new EndpointRateLimit("trainers.*", 10, 11),
            new EndpointRateLimit("abilities.*", 10, 11),
            new EndpointRateLimit("regions.*", 10, 11),
            new EndpointRateLimit("areas.*", 10, 11),
            new EndpointRateLimit("monsters.*", 10, 11),
            new EndpointRateLimit("encounters.*", 10, 11),
            new EndpointRateLimit("opponents.*", 10, 11),
            new EndpointRateLimit("items.*", 10, 11),
            new EndpointRateLimit("default", 10, 11)
    );

    private static final HashMap<String, List<Pair<Date, String>>> LAST_REQUESTS;
    private static final HashMap<String, List<Pair<Date, String>>> ALL_REQUESTS;
    private static final HashMap<String, Semaphore> SEMAPHORES;

    static {
        LAST_REQUESTS = new HashMap<>();
        ALL_REQUESTS = new HashMap<>();
        SEMAPHORES = new HashMap<>();
        ENDPOINT_RATE_LIMITS.forEach(endpoint -> SEMAPHORES.put(endpoint.endpoint(), new Semaphore(1)));
        ENDPOINT_RATE_LIMITS.forEach(endpoint -> LAST_REQUESTS.put(endpoint.endpoint(), new ArrayList<>()));
        ENDPOINT_RATE_LIMITS.forEach(endpoint -> ALL_REQUESTS.put(endpoint.endpoint(), new ArrayList<>()));
    }

    private static int historySize(EndpointRateLimit endpointLimit) {
        return ALL_REQUESTS.get(endpointLimit.endpoint()).stream()
                .filter(p -> p.getKey().getTime() >= new Date().getTime() - endpointLimit.timeFrameSeconds() * 1000L)
                .toList().size();
    }


    private static boolean wouldExceedRateLimit(EndpointRateLimit endpointLimit) {
        return historySize(endpointLimit) >= endpointLimit.maxRequests();
    }

    private static void cleanupOldRequests(EndpointRateLimit endpointLimit) {
        long currentTime = new Date().getTime();
        long timeBeginFrame = currentTime - (endpointLimit.timeFrameSeconds() * 1000L);
        ALL_REQUESTS.get(endpointLimit.endpoint()).removeIf(pair -> pair.getKey().getTime() < timeBeginFrame);
    }

    private static final int MAX_REQUESTS = 150;

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    static OkHttpClient client(TokenStorage tokenStorage) {
        Dispatcher dispatcher = new Dispatcher(new ThreadPoolExecutor(MAX_REQUESTS, MAX_REQUESTS, 1, java.util.concurrent.TimeUnit.MINUTES, new java.util.concurrent.SynchronousQueue<>()));
        dispatcher.setMaxRequests(MAX_REQUESTS);
        dispatcher.setMaxRequestsPerHost(MAX_REQUESTS);

        return new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .addInterceptor(chain -> {
                    String requestUrl = chain.request().url().toString();
                    EndpointRateLimit endpointRateLimit = ENDPOINT_RATE_LIMITS.stream()
                            .filter(endpoint -> requestUrl.matches(".*" + endpoint.endpoint() + ".*"))
                            .findFirst()
                            .orElse(ENDPOINT_RATE_LIMITS.get(ENDPOINT_RATE_LIMITS.size() - 1));

                    String endpoint = endpointRateLimit.endpoint();
                    Semaphore semaphore = SEMAPHORES.get(endpoint);
                    try {
                        semaphore.acquire();

                        while (wouldExceedRateLimit(endpointRateLimit)) {
                            if (chain.call().isCanceled()) {
                                throw new InterruptedException();
                            }

                            //noinspection BusyWait
                            Thread.sleep(100);
//                            System.out.println("waiting...");
                        }

                        cleanupOldRequests(endpointRateLimit);

                        Pair<Date, String> request = new Pair<>(new Date(), chain.request().url().toString());
                        LAST_REQUESTS.get(endpoint).add(request);
                        ALL_REQUESTS.get(endpoint).add(request);

                        final String token = tokenStorage.getAccessToken();
                        System.out.println("Request: " + chain.request().url() + " [" + endpointRateLimit.endpoint() + " - " + historySize(endpointRateLimit) + "/" + endpointRateLimit.maxRequests() + "]");
                        if (token == null) {
                            return chain.proceed(chain.request());
                        }
                        final Request newRequest = chain
                                .request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        Response response = chain.proceed(newRequest);
                        while (response.code() == 429) {
                            System.out.println("RATE LIMIT EXCEEDED\nLast " + endpointRateLimit.timeFrameSeconds() + " seconds requests were:");

                            List<Pair<Date, String>> list = ALL_REQUESTS.get(endpoint).stream()
                                    .filter(p -> p.getKey().getTime() > new Date().getTime() - endpointRateLimit.timeFrameSeconds() * 1000L)
                                    .toList();

                            for (Pair<Date, String> pair : list) {
                                System.out.println("\t" + pair.getKey() + " " + pair.getValue());
                            }

                            System.out.println("Total count: " + list.size());
                            //noinspection BusyWait
                            Thread.sleep(1000);
                            response.close();

                            semaphore.release();
                            response = chain.call().clone().execute();
                            semaphore.acquire();
                        }
                        return response;

                    } catch (InterruptedException e) {
                        System.out.println("Request was interrupted");
                        return new Response.Builder()
                                .code(500)
                                .body(ResponseBody.create(MediaType.parse("text/json"), "{}"))
                                .message("Request was interrupted")
                                .protocol(Protocol.HTTP_2)
                                .request(chain.request())
                                .build();
                    } finally {
                        semaphore.release();
                    }
                }).build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    Retrofit retrofit(OkHttpClient client, ObjectMapper objectMapper) {
        return new Retrofit.Builder()
                .baseUrl(Main.API_URL + "/")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    UserApiService user(Retrofit retrofit) {
        return retrofit.create(UserApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    AuthApiService auth(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    GroupApiService group(Retrofit retrofit) {
        return retrofit.create(GroupApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    MessageApiService message(Retrofit retrofit) {
        return retrofit.create(MessageApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    RegionApiService region(Retrofit retrofit) {
        return retrofit.create(RegionApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    AreaApiService area(Retrofit retrofit) {
        return retrofit.create(AreaApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    PresetsApiService presets(Retrofit retrofit) {
        return retrofit.create(PresetsApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    TrainerApiService trainer(Retrofit retrofit) {
        return retrofit.create(TrainerApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    AchievementsApiService achievements(Retrofit retrofit) {
        return retrofit.create(AchievementsApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    RegionEncountersApiService regionEncounters(Retrofit retrofit) {
        return retrofit.create(RegionEncountersApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    EncounterOpponentsApiService encounterOpponents(Retrofit retrofit) {
        return retrofit.create(EncounterOpponentsApiService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    TrainerItemsApiService trainerItems(Retrofit retrofit) {
        return retrofit.create(TrainerItemsApiService.class);
    }
}
