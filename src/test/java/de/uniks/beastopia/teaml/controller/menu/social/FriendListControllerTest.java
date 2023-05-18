package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.EmptyNodeQueryException;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static javafx.scene.input.KeyCode.BACK_SPACE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendListControllerTest extends ApplicationTest {

    @Spy
    Provider<FriendController> friendControllerProvider;
    @Spy
    @SuppressWarnings("unused")
    Provider<DirectMessageController> directMessageControllerProvider;
    @Mock
    FriendListService friendListService;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Preferences preferences;
    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    FriendListController friendListController;

    final List<User> users = new ArrayList<>(List.of(
            new User(null, null, "ID0", "user1", "online", null, List.of("ID1", "ID2")),
            new User(null, null, "ID1", "user2", "online", null, List.of()),
            new User(null, null, "ID2", "user3", "offline", null, List.of())
    ));

    FriendController mockedFriendController;
    DirectMessageController mockedDirectMessageController;

    @Override
    public void start(Stage stage) throws Exception {
        mockedFriendController = mock();
        mockedDirectMessageController = mock();

        ArgumentCaptor<User> userNameCaptor = ArgumentCaptor.forClass(User.class);

        when(friendControllerProvider.get()).thenReturn(mockedFriendController);

        when(mockedFriendController.setUser(userNameCaptor.capture(), anyBoolean())).thenReturn(mockedFriendController);

        when(mockedFriendController.render()).thenAnswer(invocation -> new Label(userNameCaptor.getValue().name()));
        when(mockedDirectMessageController.render()).thenReturn(new Label("DirectMessageController"));

        when(friendListService.getUsers()).thenReturn(Observable.just(users));
        when(friendListService.getFriends()).thenReturn(Observable.just(List.of(users.get(1), users.get(2))));
        when(preferences.getBoolean("ID1_pinned", true)).thenReturn(true);
        when(preferences.getBoolean("ID2_pinned", true)).thenReturn(false);

        app.start(stage);
        app.show(friendListController);
        stage.requestFocus();
    }

    @Test
    void showsFriends() {
        assertThrows(EmptyNodeQueryException.class, () -> lookup("user1").query());
        assertNotNull(lookup("user2").query());
        assertNotNull(lookup("user3").query());
    }

    @Test
    void canSearchUser3() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        write("user3");
        assertThrows(EmptyNodeQueryException.class, () -> lookup("user1").query());
        assertThrows(EmptyNodeQueryException.class, () -> lookup("user2").query());
        assertNotNull(lookup("user3").query());
    }

    @Test
    void cannotSearchUser1() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        write("user1");
        assertEquals(0, lookup("#friendList").queryAs(VBox.class).getChildren().size());
    }

    @Test
    void showsFriendsAgainAfterSearch() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));

        write("user3");
        for (int i = 0; i < 5; i++) {
            press(BACK_SPACE);
            release(BACK_SPACE);
        }
        write(" ");

        assertThrows(EmptyNodeQueryException.class, () -> lookup("user1").query());
        assertNotNull(lookup("user2").query());
        assertNotNull(lookup("user3").query());
    }

}