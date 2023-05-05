package de.uniks.beastopia.teaml.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import dagger.Module;
import dagger.Provides;
import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.TokenStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.inject.Singleton;

@Module
public class HttpModule {
    @Provides
    @Singleton
    static OkHttpClient client(TokenStorage tokenStorage) {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            final String token = tokenStorage.getAccessToken();
            if (token == null) {
                return chain.proceed(chain.request());
            }
            final Request newRequest = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = chain.proceed(newRequest);

            if (response.isSuccessful() || response.code() != 429)
                return response;

            int tryCount = 1;
            while (!response.isSuccessful() && tryCount < 10) {
                tryCount++;
                response.close();
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                response = chain.proceed(newRequest);
            }

            return response;
        }).build();
    }

    @Provides
    @Singleton
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
    UserApService user(Retrofit retrofit) {
        return retrofit.create(UserApService.class);
    }

    @Provides
    @Singleton
    AuthApiService auth(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Provides
    @Singleton
    GroupApiService group(Retrofit retrofit) {
        return retrofit.create(GroupApiService.class);
    }

    @Provides
    @Singleton
    MessageApiService message(Retrofit retrofit) {
        return retrofit.create(MessageApiService.class);
    }

    @Provides
    @Singleton
    RegionApiService region(Retrofit retrofit) {
        return retrofit.create(RegionApiService.class);
    }
}
