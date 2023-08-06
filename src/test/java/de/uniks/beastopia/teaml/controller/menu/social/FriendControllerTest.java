package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_OFFLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest extends ApplicationTest {

    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    final User testUser = new User(null, null, null, "Test", STATUS_OFFLINE, null, null);
    @Spy
    App app;
    @Mock
    FriendListService friendListService;
    @Mock
    EventListener eventListener;
    @Mock
    Prefs prefs;
    @SuppressWarnings("unused")
    @Mock
    DataCache cache;
    @InjectMocks
    FriendController friendController;
    @Spy
    Provider<DirectMessageController> directMessageControllerProvider;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(friendListService.isFriend(testUser)).thenReturn(true);
        when(eventListener.listen(anyString(), any())).thenReturn(Observable.empty());
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

        verify(friendListService, times(3)).isFriend(testUser);
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

        verify(friendListService, times(3)).isFriend(testUser);
        verify(friendListService).removeFriend(testUser);
        assertEquals(testUser, changedUser.get());
    }

    @Test
    void openFriendChat() {
        DirectMessageController mock = mock(DirectMessageController.class);

        when(directMessageControllerProvider.get()).thenReturn(mock);

        friendController.openFriendChat();

        verify(mock).setupDirectMessageController(testUser);
    }

    @Test
    public void pinFriendTest() {
        final ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        final ArgumentCaptor<Boolean> booleanCaptor = ArgumentCaptor.forClass(Boolean.class);
        doNothing().when(prefs).setPinned(userCaptor.capture(), booleanCaptor.capture());

        clickOn("#pin");
        assertTrue(booleanCaptor.getValue());
        verify(prefs, times(1)).setPinned(userCaptor.getValue(), booleanCaptor.getValue());
    }
}