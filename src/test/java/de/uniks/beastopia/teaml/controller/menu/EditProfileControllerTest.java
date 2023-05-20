package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

class EditProfileControllerTest extends ApplicationTest {
    @Mock
    Prefs prefs;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Mock
    Provider<DeleteUserController> deleteUserControllerProvider;
    @Mock
    Provider<PauseController> pauseControllerProvider;
    @Mock
    AuthService authService;
    @Mock
    TokenStorage tokenStorage;
    @Spy
    ThemeSettings themeSettings;
    @Spy
    App app;
    @Spy
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    @InjectMocks
    EditProfileController editProfileController;


    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app, prefs);
        when(prefs.getTheme()).thenReturn("dark");

        User mockedUser = mock(User.class);
        when(tokenStorage.getCurrentUser()).thenReturn(mockedUser);
        when(mockedUser.name()).thenReturn("Alice");

        app.start(stage);
        app.show(editProfileController);
        stage.requestFocus();
    }

    @Test
    public void setDarkTheme() {
        doNothing().when(prefs).setTheme(any());
        Consumer<String> mocked = mock();
        doNothing().when(mocked).accept(any());
        themeSettings.updateSceneTheme = mocked;

        clickOn("#summerRadioButton");
        clickOn("#darkRadioButton");

        verify(prefs, times(1)).setTheme("dark");
        verify(mocked, times(1)).accept("dark");
    }

    @Test
    public void setSummerTheme() {
        doNothing().when(prefs).setTheme(any());
        Consumer<String> mocked = mock();
        doNothing().when(mocked).accept(any());
        themeSettings.updateSceneTheme = mocked;

        clickOn("#summerRadioButton");

        verify(prefs, times(1)).setTheme("summer");
        verify(mocked, times(1)).accept("summer");
    }

    @Test
    void title() {
        assertEquals(resources.getString("titleEditProfile"), app.getStage().getTitle());
    }


}