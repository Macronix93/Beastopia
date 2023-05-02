package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.UserAPIService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class FriendService {

    @Inject
    UserAPIService userAPIService;

    @Inject
    public FriendService() {
    }

    public Observable<List<User>> getFriends(List<String> ids) {
        return userAPIService.getFriends(ids);
    }
}
