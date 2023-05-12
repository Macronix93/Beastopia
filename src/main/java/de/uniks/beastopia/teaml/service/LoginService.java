package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.AuthApiService;
import de.uniks.beastopia.teaml.rest.LoginDto;
import de.uniks.beastopia.teaml.rest.LoginResult;
import de.uniks.beastopia.teaml.rest.RefreshDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.prefs.Preferences;

public class LoginService {
    private final TokenStorage tokenStorage;
    private final AuthApiService authApiService;
    private final Preferences preferences;

    @Inject
    public LoginService(TokenStorage tokenStorage, AuthApiService authApiService, Preferences preferences) {
        this.tokenStorage = tokenStorage;
        this.authApiService = authApiService;
        this.preferences = preferences;
    }

    public Observable<LoginResult> login(String username, String password, boolean rememberMe) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            if (rememberMe) {
                preferences.put("rememberMe", lr.refreshToken());
            }
            return lr;
        });
    }

    public boolean isRememberMe() {
        return preferences.get("rememberMe", null) != null;
    }

    public Observable<LoginResult> refresh() {
        return authApiService.refresh(new RefreshDto(preferences.get("rememberMe", null))).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            return lr;
        });
    }
}
