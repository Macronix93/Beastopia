package de.uniks.beastopia.teaml.rest;

import de.uniks.beastopia.teaml.model.User;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAPIService {
    @POST("users")
    Observable<User> createUser(@Body CreateUserDto userDto);

    @GET("users")
    ObservableList<User> getFriends();
}
