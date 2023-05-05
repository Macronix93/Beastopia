package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateUserDto;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class RegistrationService {

    @Inject
    UserApiService userAPIService;

    @Inject
    public RegistrationService() {
    }

    public Observable<User> createUser(String name, String avatar, String password) {
        return userAPIService.createUser(new CreateUserDto(name, avatar, password));
    }

}
