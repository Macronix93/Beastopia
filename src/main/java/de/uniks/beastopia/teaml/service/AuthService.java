package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.utils.Prefs;
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
    Prefs prefs;

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
                prefs.setRememberMe(lr.refreshToken());
            }
            return lr;
        });
    }

    public Observable<LoginResult> refresh() {
        if (!prefs.isRememberMe()) {
            return Observable.error(new RuntimeException("No refresh token"));
        }

        return authApiService.refresh(new RefreshDto(prefs.getRememberMeToken())).map(lr -> {
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
            prefs.clearRememberMe();
            authApiService.logout().subscribe();
            return tokenStorage.getCurrentUser();
        });
    }

    public Observable<User> goOffline() {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, UserApiService.STATUS_OFFLINE, null, null, null)).map(user -> tokenStorage.getCurrentUser());
    }

    public Observable<User> updatePassword(String password) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, null, null, null, password));
    }

    public Observable<User> updateUsername(String username) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(username, null, null, null, null)).map(user -> {
            tokenStorage.setCurrentUser(user);
            return user;
        });
    }

    public Observable<User> updateUsernameAndPassword(String username, String password) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(username, null, null, null, password)).map(user -> {
            tokenStorage.setCurrentUser(user);
            return user;
        });
    }

    @SuppressWarnings("unused")
    public Observable<User> updateAvatar(String avatar) {
        return userApiService.updateUser(tokenStorage.getCurrentUser()._id(), new UpdateUserDto(null, null, avatar, null, null)).map(user -> {
            tokenStorage.setCurrentUser(user);
            return user;
        });
    }

    public Observable<User> deleteUser() {
        return Observable.create(source -> {
            userApiService.deleteUser(tokenStorage.getCurrentUser()._id()).subscribe();
            source.onNext(tokenStorage.getCurrentUser());
        });
    }
}
