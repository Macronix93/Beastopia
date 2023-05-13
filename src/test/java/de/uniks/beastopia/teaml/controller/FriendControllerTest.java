package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.atomic.AtomicReference;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_OFFLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest extends ApplicationTest {

    @Spy
    App app;

    @Mock
    FriendListService friendListService;

    @InjectMocks
    FriendController friendController;

    User testUser = new User(null, null, null, "Test", STATUS_OFFLINE, null, null);

    @Override
    public void start(Stage stage) throws Exception {
        when(friendListService.isFriend(testUser)).thenReturn(true);
        friendController.setUser(testUser, false);

        app.start(stage);
        app.show(friendController);
        stage.requestFocus();
    }

    @Test
    void addFriend() {
        when(friendListService.isFriend(testUser)).thenReturn(false);
        AtomicReference<User> changedUser = new AtomicReference<>(null);
        friendController.setOnFriendChanged(changedUser::set);
        when(friendListService.addFriend(testUser)).thenReturn(Observable.just(testUser));

        clickOn("#addRemoveFriendButton");

        verify(friendListService, times(2)).isFriend(testUser);
        verify(friendListService).addFriend(testUser);
        assertEquals(testUser, changedUser.get());
    }

    @Test
    void removeFriend() {
        when(friendListService.isFriend(testUser)).thenReturn(true);
        AtomicReference<User> changedUser = new AtomicReference<>(null);
        friendController.setOnFriendChanged(changedUser::set);
        when(friendListService.removeFriend(testUser)).thenReturn(Observable.just(testUser));

        clickOn("#addRemoveFriendButton");

        verify(friendListService, times(2)).isFriend(testUser);
        verify(friendListService).removeFriend(testUser);
        assertEquals(testUser, changedUser.get());
    }

    @Test
    void openFriendChat() {
        // TODO
    }
}