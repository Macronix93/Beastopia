package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.AuthApiService;
import de.uniks.beastopia.teaml.rest.LoginDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class LoginService {
    @Inject
    AuthApiService authApiService;

    @Inject
    public LoginService() {
    }

    public Observable<User> login(String name, String password) {
        return authApiService.login(new LoginDto(name, password));
    }
}
