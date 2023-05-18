package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.MainComponent;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.utils.Prefs;
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
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngameControllerTest extends ApplicationTest {

    @Mock
    Provider<PauseController> pauseControllerProvider;

    @Spy
    App app;
    @Spy
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        MainComponent mockedMainComponent = mock();
        LoginController mockedLoginController = mock();
        when(mockedLoginController.render()).thenReturn(new Label());
        Prefs mockedPrefs = mock(Prefs.class);
        when(mockedPrefs.isRememberMe()).thenReturn(false);
        when(mockedMainComponent.loginController()).thenReturn(mockedLoginController);
        when(mockedMainComponent.prefs()).thenReturn(mockedPrefs);
        app.setMainComponent(mockedMainComponent);

        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void pauseMenu() {
        final PauseController mock = Mockito.mock(PauseController.class);
        when(pauseControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        press(KeyCode.ESCAPE);

        verify(app).show(mock);
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleIngame"));
    }
}