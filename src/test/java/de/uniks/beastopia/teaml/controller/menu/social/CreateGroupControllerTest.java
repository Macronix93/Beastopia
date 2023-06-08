package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.Node;
import javafx.scene.text.Text;
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
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CreateGroupControllerTest extends ApplicationTest {

    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
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
    private final List<User> addedUsersList = new ArrayList<>();
    private final List<Controller> addedUserControllers = new ArrayList<>();
    private final List<Controller> availableUserControllers = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        app.start(stage);
        app.show(createGroupController);
        stage.requestFocus();
    }

    @Test
    void updateUserList() {


    }

    @Test
    void updateAddedUserList() {
    }

    @Test
    void createGroup() {
        assertEquals(createGroupController.groupNameField.getText().isEmpty(), createGroupController.groupNameField.getText().isEmpty());
        clickOn("#createGrpButton");
        Node dialogPane = lookup(".dialog-pane").query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("Groupname missing")).query();
        clickOn("OK");
        /*
        clickOn("#chatInput");
        write("Hello World");
        clickOn("#sendButton");
        */


    }

    @Test
    void back() {
        clickOn("#backButton");
    }

    @Test
    void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleNewGroup"));
    }
}
