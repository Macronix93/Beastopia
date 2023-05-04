package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.UserAPIService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class UserService {

    @Inject
    UserAPIService userAPIService;
    @Inject
    public UserService() {
    }
    public Observable<User> getUser(String id) {
        return userAPIService.getUser(id);
    }
}
