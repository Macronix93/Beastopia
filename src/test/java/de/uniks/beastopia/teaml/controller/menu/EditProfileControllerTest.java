package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EditProfileControllerTest extends ApplicationTest {
    @Mock
    Prefs prefs;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Mock
    @SuppressWarnings("unused")
    Provider<DeleteUserController> deleteUserControllerProvider;
    @Mock
    @SuppressWarnings("unused")
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

    User user;


    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app, prefs);
        when(prefs.getTheme()).thenReturn("dark");

        user = new User(null, null, null, "Alice", null, null, null);
        when(tokenStorage.getCurrentUser()).thenReturn(user);

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
    public void changePasswordSuccessfully() {
        when(authService.updatePassword("12345678")).thenReturn(Observable.just(user));
        MenuController mocked = mock();
        when(menuControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());

        clickOn("#passwordInput");
        write("12345678");
        clickOn("#passwordRepeatInput");
        write("12345678");
        clickOn("#changePasswordButton");

        verify(authService).updatePassword("12345678");
        verify(menuControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    public void changePasswordNotMatching() {
        clickOn("#passwordInput");
        write("123456789");
        clickOn("#passwordRepeatInput");
        write("12345678");
        clickOn("#changePasswordButton");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("not equal")).query();
        assertNotNull(result);
    }

    @Test
    public void changePasswordInvalid() {

    }

    @Test
    public void deleteUser() {
        DeleteUserController mocked = mock();
        when(deleteUserControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());

        clickOn("#deleteUserButton");

        verify(deleteUserControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    public void backMenu() {
        editProfileController.backController("menu");
        MenuController mocked = mock();
        when(menuControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());

        clickOn("#backButton");

        verify(menuControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    public void backPause() {
        editProfileController.backController("pause");
        PauseController mocked = mock();
        when(pauseControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());

        clickOn("#backButton");

        verify(pauseControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    void title() {
        assertEquals(resources.getString("titleEditProfile"), app.getStage().getTitle());
    }
}