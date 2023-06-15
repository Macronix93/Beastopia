package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.GroupListService;
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

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditGroupControllerTest extends ApplicationTest {
    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @SuppressWarnings("unused")
    @Mock
    Prefs prefs;
    @Mock
    Provider<DirectMessageController> directMessageControllerProvider;
    @Mock
    Provider<UserController> userControllerProvider;
    @Mock
    GroupListService groupListService;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    DataCache cache;

    @InjectMocks
    EditGroupController editGroupController;

    final List<User> users = new ArrayList<>(List.of(
            new User(null, null, "ID0", "user1", "online", null, List.of("ID1", "ID2")),
            new User(null, null, "ID1", "user2", "online", null, List.of()),
            new User(null, null, "ID2", "user3", "offline", null, List.of())
    ));
    final Group group = new Group(null, null, "ID0", "group1", List.of("ID0", "ID1", "ID2"));
    final UserController mockedUserController1 = mock();
    final UserController mockedUserController2 = mock();
    final UserController mockedUserController3 = mock();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        Label testLabel1 = new Label("Label1");
        testLabel1.setId("testLabelID1");

        Label testLabel2 = new Label("Label2");
        testLabel2.setId("testLabelID2");

        Label testLabel3 = new Label("Label3");
        testLabel3.setId("testLabelID3");

        AtomicInteger callGet = new AtomicInteger(0);
        when(userControllerProvider.get()).thenAnswer(invocation -> {
            if (callGet.get() == 0) {
                callGet.set(1);
                return mockedUserController1;
            } else if (callGet.get() == 1) {
                callGet.set(2);
                return mockedUserController2;
            } else {
                callGet.set(0);
                return mockedUserController3;
            }
        });
        when(mockedUserController1.setIsAdded(anyBoolean())).thenReturn(mockedUserController1);
        when(mockedUserController2.setIsAdded(anyBoolean())).thenReturn(mockedUserController2);
        when(mockedUserController3.setIsAdded(anyBoolean())).thenReturn(mockedUserController3);

        doNothing().when(mockedUserController1).setUser(any(User.class));
        when(mockedUserController1.render()).thenReturn(testLabel1);
        doNothing().when(mockedUserController2).setUser(any(User.class));
        when(mockedUserController2.render()).thenReturn(testLabel2);
        doNothing().when(mockedUserController3).setUser(any(User.class));
        when(mockedUserController3.render()).thenReturn(testLabel3);


        editGroupController.setGroup(group);
        when(cache.getUser("ID0")).thenReturn(users.get(0));
        when(cache.getUser("ID1")).thenReturn(users.get(1));
        when(cache.getUser("ID2")).thenReturn(users.get(2));

        app.start(stage);
        app.show(editGroupController);
        stage.requestFocus();
    }


    @Test
    void getTitleTest() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEditGroup"));
    }

    @Test
    void updateUserList() {
        when(cache.getAllUsers()).thenReturn(List.of(new User(null, null, "ID3", "user3", "offline", null, List.of())));
        clickOn("#usernameField");
        write("user3");
        VBox users = lookup("#users").query();
        assertEquals(1, users.getChildren().size());
    }

    @Test
    void backTest() {
        DirectMessageController mocked = mock(DirectMessageController.class);
        when(directMessageControllerProvider.get()).thenReturn(mocked);
        doNothing().when(app).show(mocked);
        clickOn("#testBack");
        verify(directMessageControllerProvider, times(1)).get();
    }


    @Test
    void editGroupTest() {
        when(tokenStorage.getCurrentUser()).thenReturn(users.get(0));
        when(groupListService.updateGroup(any(Group.class))).thenReturn(Observable.just(group));
        clickOn("#testEditGroup");
        verify(tokenStorage, times(1)).getCurrentUser();
        verify(groupListService, times(1)).updateGroup(any(Group.class));
    }
}