package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import javafx.scene.control.Label;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PauseControllerTest extends ApplicationTest {

    @Mock
    Provider<FriendListController> friendListControllerProvider;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Spy
    App app;
    @InjectMocks
    PauseController pauseController;

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    FriendListController mockedFriendListController;
    MenuController mockedMenuController;

    @Override
    public void start(Stage stage) throws Exception {
        mockedFriendListController = mock();
        mockedMenuController = mock();

        when(mockedFriendListController.render()).thenReturn(new Label("FriendListController"));

        when(friendListControllerProvider.get()).thenReturn(mockedFriendListController);

        app.start(stage);
        app.show(pauseController);
        stage.requestFocus();
    }

    @Test
    void showsFriendList() {
        assertNotNull(lookup("FriendListController").query());
        verify(mockedFriendListController).render();
    }

    @Test
    void openMenu() {
        when(mockedMenuController.render()).thenReturn(new Label("MenuController"));
        when(menuControllerProvider.get()).thenReturn(mockedMenuController);
        clickOn("#mainMenuButton");
        assertNotNull(lookup("MenuController").query());
        verify(mockedMenuController).render();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titlePause"));
    }
}