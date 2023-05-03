package de.uniks.beastopia.teaml.rest;

import de.uniks.beastopia.teaml.model.User;
import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface UserAPIService {
    @POST("users")
    Observable<User> createUser(@Body CreateUserDto userDto);
}
