package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PauseControllerTest extends ApplicationTest {

    @Mock
    Provider<FriendListController> friendListControllerProvider;

    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Spy
    App app;
    @InjectMocks
    PauseController pauseController;

    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    FriendListController mockedFriendListController;
    MenuController mockedMenuController;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

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

    @Test
    void pauseMenu() {
        final IngameController mock = Mockito.mock(IngameController.class);
        doNothing().when(app).show(mock);

        app.setHistory(List.of(mock));

        press(KeyCode.ESCAPE);
        sleep(1000);

        verify(app).show(mock);
    }
}