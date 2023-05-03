package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.UserAPIService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;

public class FriendService {
    @Inject
    UserAPIService userAPIService;

    @Inject
    public FriendService() {
    }

    public Observable<User> getFriend(String id) {
        return userAPIService.getFriend(id)
                .map(f -> {
                    User user = new User();
                    user.setId(f.getId())
                            .setName(f.getName())
                            .setStatus(f.getStatus())
                            .setAvatar(f.getAvatar());
                    return user;
                });
    }
}
