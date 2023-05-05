package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class AuthService {
    @Inject
    TokenStorage tokenStorage;
    @Inject
    AuthApiService authApiService;
    @Inject
    UserApiService userApiService;

    @Inject
    public AuthService() {
    }

    public Observable<LoginResult> login(String username, String password) {
        return authApiService.login(new LoginDto(username, password)).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            tokenStorage.setRefreshToken(lr.refreshToken());
            tokenStorage.setCurrentUser(new User(lr.createdAt(), lr.updatedAt(), lr._id(), lr.name(), lr.status(), lr.avatar(), lr.friends()));
            userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, UserApiService.STATUS_ONLINE, null, null, null)).subscribe();
            return lr;
        });
    }

    public Observable<LoginResult> refresh() {
        return authApiService.refresh(new RefreshDto(tokenStorage.getRefreshToken())).map(lr -> {
            tokenStorage.setAccessToken(lr.accessToken());
            tokenStorage.setRefreshToken(lr.refreshToken());
            tokenStorage.setCurrentUser(new User(lr.createdAt(), lr.updatedAt(), lr._id(), lr.name(), lr.status(), lr.avatar(), lr.friends()));
            userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, UserApiService.STATUS_ONLINE, null, null, null)).subscribe();
            return lr;
        });
    }

    public Observable<Void> logout() {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, UserApiService.STATUS_OFFLINE, null, null, null)).map(user -> {
            return authApiService.logout().blockingFirst();
        });
    }
}
