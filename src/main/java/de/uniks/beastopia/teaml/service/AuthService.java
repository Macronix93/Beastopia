package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.prefs.Preferences;

public class AuthService {
    @Inject
    TokenStorage tokenStorage;
    @Inject
    AuthApiService authApiService;
    @Inject
    UserApiService userApiService;
    @Inject
    Preferences preferences;

    @Inject
    public AuthService() {
    }

    public Observable<LoginResult> login(String username, String password, boolean rememberMe) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            tokenStorage.setRefreshToken(lr.refreshToken());
            tokenStorage.setCurrentUser(userApiService.updateUser(lr._id(), new UpdateUserDto(null,
                    UserApiService.STATUS_ONLINE, null, null, null)).blockingFirst());
            if (rememberMe) {
                preferences.put("rememberMe", lr.refreshToken());
            }
            return lr;
        });
    }

    public Observable<LoginResult> refresh() {
        return authApiService.refresh(new RefreshDto(preferences.get("rememberMe", null))).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            tokenStorage.setRefreshToken(lr.refreshToken());
            tokenStorage.setCurrentUser(userApiService.updateUser(lr._id(), new UpdateUserDto(null,
                    UserApiService.STATUS_ONLINE, null, null, null)).blockingFirst());
            return lr;
        });
    }

    public Observable<User> logout() {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null,
                UserApiService.STATUS_OFFLINE, null, null, null)).map(user -> {
            preferences.remove("rememberMe");
            authApiService.logout().subscribe();
            return tokenStorage.getCurrentUser();
        });
    }

    public boolean isRememberMe() {
        return preferences.get("rememberMe", null) != null;
    }

    public Observable<User> updatePassword(String password) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, null, null, null, password));
    }

    public Observable<User> updateAvatar(String avatar) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, null, avatar, null, null));
    }

    public Observable<Void> deleteUser() {
        return userApiService.deleteUser(tokenStorage.getCurrentUser()._id());
    }
}
