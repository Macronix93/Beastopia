package de.uniks.beastopia.teaml.rest;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("users")
    Observable<User> createUser(@Body CreateUserDto userDto);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);
}
