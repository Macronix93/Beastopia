package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.UserAPIService;
import de.uniks.beastopia.teaml.rest.CreateUserDto;
import de.uniks.beastopia.teaml.model.User;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class RegistrationService {

    @Inject
    UserAPIService userAPIService;

    @Inject
    public RegistrationService() {}

    public Observable<User> createUser(String name, String avatar, String password) {
        return userAPIService.createUser(new CreateUserDto(name, avatar, password));
    }

}