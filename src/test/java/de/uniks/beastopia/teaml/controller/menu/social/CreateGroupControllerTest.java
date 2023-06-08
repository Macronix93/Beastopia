package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
class CreateGroupControllerTest extends ApplicationTest {

    @Spy
    App app;
    @Spy
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    CreateGroupController createGroupController;
    @Mock
    Provider<DirectMessageController> directMessageControllerProvider;
    @Mock
    Provider<UserController> userControllerProvider;
    @Mock
    Provider<CreateGroupController> createGroupControllerProvider;
    @Mock
    GroupListService groupListService;
    @Mock
    FriendListService friendListService;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Prefs prefs;
    @Mock
    DataCache cache;

    final User userOne = new User(null, null, "1", "1", null, null, null);
    final User userTwo = new User(null, null, "2", "2", null, null, null);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        app.start(stage);
        app.show(createGroupController);
        stage.requestFocus();
    }

    @Test
    void getTitle() {
    }

    @Test
    void updateUserList() {
    }

    @Test
    void updateAddedUserList() {
    }

    @Test
    void back() {
    }

    @Test
    void createGroup() {
    }
}