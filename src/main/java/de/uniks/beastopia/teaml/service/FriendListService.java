package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.AuthApiService;
import de.uniks.beastopia.teaml.rest.UpdateUserDto;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FriendListService {

    public static enum Status {
        Online,
        Offline
    }

    @Inject
    TokenStorage tokenStorage;

    @Inject
    UserApiService userApiService;
    @Inject
    AuthApiService authApiService;

    @Inject
    public FriendListService() {
    }

    public Observable<List<User>> getUsers() {
        return userApiService.getUsers(null, null);
    }

    public Observable<List<String>> getFriendIDs() {
        return userApiService
                .getUser(tokenStorage.getCurrentUser()._id())
                .map(User::friends);
    }

    public Observable<List<User>> getFriends() {
        return userApiService
                .getUsers(tokenStorage.getCurrentUser().friends(), null);
    }

    public Observable<List<User>> getFriends(Status status) {
        return userApiService
                .getUsers(tokenStorage.getCurrentUser().friends(), switch (status) {
                    case Online -> "online";
                    case Offline -> "offline";
                });
    }

    public Observable<User> addFriend(User friend) {
        return getFriendIDs().map(friends -> {
            List<String> friendsCopy = new ArrayList<>(friends);
            friendsCopy.add(friend._id());
            String userID = tokenStorage.getCurrentUser()._id();
            tokenStorage.setCurrentUser(userApiService.updateUser(userID, new UpdateUserDto(null, null, null, friendsCopy, null)).blockingFirst());
            return tokenStorage.getCurrentUser();
        });
    }

    public Observable<User> removeFriend(User friend) {
        return getFriendIDs().map(friends -> {
            List<String> friendsCopy = new ArrayList<>(friends);
            friendsCopy.remove(friend._id());
            String userID = tokenStorage.getCurrentUser()._id();
            tokenStorage.setCurrentUser(userApiService.updateUser(userID, new UpdateUserDto(null, null, null, friendsCopy, null)).blockingFirst());
            return tokenStorage.getCurrentUser();
        });
    }
}
