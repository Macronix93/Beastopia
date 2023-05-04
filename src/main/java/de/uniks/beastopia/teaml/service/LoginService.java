package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.LoginResult;
import de.uniks.beastopia.teaml.rest.LoginDto;
import de.uniks.beastopia.teaml.rest.AuthApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class LoginService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;

    @Inject
    public LoginService(TokenStorage tokenStorage, AuthApiService authApiService) {
        this.tokenStorage = tokenStorage;
        this.authApiService = authApiService;
    }

    public Observable<LoginResult> login(String username, String password) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setAccessToken(lr.getAccessToken());
            tokenStorage.setRefreshToken(lr.getRefreshToken());
            return lr;
        });
    }
}
