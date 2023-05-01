package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.dto.CreateUserDto;
import de.uniks.beastopia.teaml.model.User;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPIService {
    @POST("users")
    Observable<User> createUser(@Body CreateUserDto userDto);
}
