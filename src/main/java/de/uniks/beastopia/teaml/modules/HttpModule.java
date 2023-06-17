package de.uniks.beastopia.teaml.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.util.Pair;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;


@Module
public class HttpModule {
    private static final List<String> RATE_LIMIT_FREE_ENDPOINTS = List.of("presets/");

    private static final int MAX_REQUESTS = 9;
    private static final int MAX_REQUESTS_TIME_FRAME_SECONDS = 12;
    private static final List<Pair<Date, String>> LAST_REQUESTS = new ArrayList<>();
    private static final List<Pair<Date, String>> ALL_REQUESTS = new ArrayList<>();
    private static final Semaphore SEMAPHORE = new Semaphore(1);

    private static int getRequestsLastTimeFrame() {
        long currentTime = new Date().getTime();
        long timeBeginFrame = currentTime - (MAX_REQUESTS_TIME_FRAME_SECONDS * 1000);
        LAST_REQUESTS.removeIf(pair -> pair.getKey().getTime() < timeBeginFrame);
        return LAST_REQUESTS.size();
    }

    private static long getRequiredDelay() {
        long currentTime = new Date().getTime();
        LAST_REQUESTS.sort((o1, o2) -> (int) (o1.getKey().getTime() - o2.getKey().getTime()));
        long timeOfFirstRequest = LAST_REQUESTS.get(0).getKey().getTime();
        return (MAX_REQUESTS_TIME_FRAME_SECONDS * 1000) - (currentTime - timeOfFirstRequest);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    static OkHttpClient client(TokenStorage tokenStorage) {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    if (RATE_LIMIT_FREE_ENDPOINTS.stream().anyMatch(chain.request().url().toString()::contains)) {
                        System.out.println("Request: " + chain.request().url());
                        return chain.proceed(chain.request());
                    }

                    try {
                        SEMAPHORE.acquire();
                        if (getRequestsLastTimeFrame() >= MAX_REQUESTS) {
                            long timeToSleep = getRequiredDelay();
                            if (timeToSleep > 0) {
                                Thread.sleep(timeToSleep);
                            } else {
                                System.out.println("TIME CALCULATION WRONG?!");
                            }
                        }
                        Pair<Date, String> request = new Pair<>(new Date(), chain.request().url().toString());
                        LAST_REQUESTS.add(request);
                        ALL_REQUESTS.add(request);

                        final String token = tokenStorage.getAccessToken();
                        if (token == null) {
                            System.out.println("Request: " + chain.request().url());
                            return chain.proceed(chain.request());
                        }
                        final Request newRequest = chain
                                .request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer " + token)
                                .build();

                        System.out.println("Request: " + chain.request().url());
                        Response response = chain.proceed(newRequest);
                        if (response.code() == 429) {
                            System.out.println("RATE LIMIT EXCEEDED\nLast 15 seconds requests were:");

                            for (Pair<Date, String> pair : ALL_REQUESTS.stream().filter(p -> p.getKey().getTime() > new Date().getTime() - 15000).toList()) {
                                System.out.println("\t" + pair.getKey() + " " + pair.getValue());
                            }
                            System.exit(1);
                        }
                        return response;

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        SEMAPHORE.release();
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
}
