package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.UserAPIService;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ObservableList;

import javax.inject.Inject;

public class FriendService {
    @Inject
    UserAPIService userAPIService;

    @Inject
    public FriendService() {
    }

    //TODO public ObservableList<User> getFriends()
}
