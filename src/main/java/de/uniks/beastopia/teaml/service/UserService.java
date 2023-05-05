package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class UserService {

    @Inject
    UserApiService userAPIService;
    @Inject
    public UserService() {
    }
    public Observable<User> getUser(String id) {
        return userAPIService.getUser(id);
    }
}
