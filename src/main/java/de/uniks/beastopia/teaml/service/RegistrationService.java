package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.dto.CreateUserDto;
import de.uniks.beastopia.teaml.model.User;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class RegistrationService {

    @Inject
    AuthAPIService authAPIService;

    @Inject
    public RegistrationService() {}

    public Observable<User> createUser(String name, String avatar, String password) {
        return authAPIService.createUser(new CreateUserDto(name, avatar, password));
    }

}
