package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
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

import java.util.ResourceBundle;


@ExtendWith(MockitoExtension.class)
class ChatUserControllerTest extends ApplicationTest {

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @SuppressWarnings("unused")
    @Mock
    TokenStorage tokenStorage;
    @SuppressWarnings("unused")
    @Mock
    FriendListService friendListService;
    @SuppressWarnings("unused")
    @Mock
    Prefs prefs;
    @SuppressWarnings("unused")
    @Mock
    DataCache cache;
    @Spy
    App app;
    @InjectMocks
    ChatUserController chatUserController;

    @SuppressWarnings("unused")
    final Group testGrp = new Group(null, null, "1", "1", null);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        app.start(stage);
        app.show(chatUserController);
        stage.requestFocus();
    }

    @Test
    public void render() {

    }

}