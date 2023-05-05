package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface UserApService {
    public static final String STATUS_ONLINE = "online";
    public static final String STATUS_OFFLINE = "offline";

    @POST("users")
    Observable<User> createUser(@Body CreateUserDto userDto);

    @GET("users")
    Observable<List<User>> getUsers(@Query("ids") List<String> ids, @Query("status") String status);

    @GET("users/{id}")
    Observable<User> getUser(@Path("id") String id);

    @PATCH("users/{id}")
    Observable<User> updateUser(@Path("id") String id, @Body UpdateUserDto userDto);

    @DELETE("users/{id}")
    Observable<Void> deleteUser(@Path("id") String id);
}
