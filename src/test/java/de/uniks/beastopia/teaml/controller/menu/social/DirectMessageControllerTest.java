package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.sockets.EventListener;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectMessageControllerTest extends ApplicationTest {

    @Spy
    App app = new App(null);

    @Mock
    Provider<MenuController> menuControllerProvider;

    @InjectMocks
    DirectMessageController directMessageController;

    @Mock
    ChatListController chatListController;

    @Mock
    EventListener eventListener;

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        when(chatListController.render()).thenReturn(new Label("chatList"));

        app.start(stage);
        app.show(directMessageController);
        stage.requestFocus();
    }

    @Test
    void back() {
        final MenuController mock = Mockito.mock(MenuController.class);
        when(menuControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        clickOn("#backButton");

        verify(app).show(mock);
    }

    @Test
    void newGroup() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleDirectMessage"));
    }

}