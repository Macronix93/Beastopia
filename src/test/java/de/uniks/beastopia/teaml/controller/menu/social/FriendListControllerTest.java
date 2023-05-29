package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.input.KeyCode.BACK_SPACE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @Spy
    @SuppressWarnings("unused")
    DataCache cache;
    @Mock
    Prefs prefs;
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

    FriendController mockedFriendController1;
    FriendController mockedFriendController2;
    DirectMessageController mockedDirectMessageController;
    Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        AppPreparer.prepare(app);

        mockedFriendController1 = mock();
        mockedFriendController2 = mock();
        mockedDirectMessageController = mock();

        AtomicInteger call = new AtomicInteger(0);
        when(friendControllerProvider.get()).thenAnswer(invocation -> {
            if (call.getAndIncrement() % 2 == 0) {
                return mockedFriendController1;
            } else {
                return mockedFriendController2;
            }
        });

        when(mockedFriendController1.setUser(any(), anyBoolean())).thenReturn(mockedFriendController1);
        when(mockedFriendController1.render()).thenAnswer(invocation -> new Label(users.get(1).name()));
        when(mockedFriendController2.setUser(any(), anyBoolean())).thenReturn(mockedFriendController2);
        when(mockedFriendController2.render()).thenAnswer(invocation -> new Label(users.get(2).name()));

        when(mockedDirectMessageController.render()).thenReturn(new Label("DirectMessageController"));

        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        when(friendListService.getUsers()).thenReturn(Observable.just(users));
        when(friendListService.getFriendIDs()).thenReturn(List.of("ID1", "ID2"));
        when(prefs.isPinned(users.get(1))).thenReturn(true);
        when(prefs.isPinned(users.get(2))).thenReturn(false);

        app.start(stage);
        app.show(friendListController);
        stage.requestFocus();
    }

    @Test
    void showsFriends() {
        sleep(1000);

        assertThrows(EmptyNodeQueryException.class, () -> lookup("user1").query());
        assertNotNull(lookup("user2").query());
        assertNotNull(lookup("user3").query());
    }

    @Test
    void canSearchUser3() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        write("user3");
        sleep(2000);
        assertTrue(lookup("user1").queryAll().isEmpty());
        assertTrue(lookup("user2").queryAll().isEmpty());
        assertNotNull(lookup("user3").query());
    }

    @Test
    void cannotSearchUser1() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        write("user1");
        sleep(1000);
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
        sleep(1000);

        sleep(100);

        assertThrows(EmptyNodeQueryException.class, () -> lookup("user1").query());
        assertNotNull(lookup("user2").query());
        assertNotNull(lookup("user3").query());
    }

}