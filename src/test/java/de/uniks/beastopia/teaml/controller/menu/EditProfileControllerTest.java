package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import retrofit2.HttpException;
import retrofit2.Response;

import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EditProfileControllerTest extends ApplicationTest {
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    @Mock
    DataCache cache;
    @Spy
    App app;
    @Spy
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    @InjectMocks
    EditProfileController editProfileController;

    User user;


    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        user = new User(null, null, null, "Alice", null, null, null);
        when(tokenStorage.getCurrentUser()).thenReturn(user);

        app.start(stage);
        app.show(editProfileController);
        stage.requestFocus();
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
        clickOn("#editProfileButton");

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
        clickOn("#editProfileButton");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("not equal")).query();
        assertNotNull(result);
    }

    @Test
    public void changePasswordInvalid() {
        ResponseBody body = ResponseBody.create(MediaType.get("application/json"), "{\"message\":\"At least 8 characters.\"}");
        when(authService.updatePassword(anyString())).thenReturn(Observable.error(new HttpException(Response.error(400, body))));

        clickOn("#passwordInput");
        write("1234");
        clickOn("#passwordRepeatInput");
        write("1234");
        clickOn("#editProfileButton");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("At least 8")).query();
        assertNotNull(result);
    }

    @Test
    public void deleteUser() {
        DeleteUserController mocked = mock();
        when(deleteUserControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());
        when(mocked.backController(any())).thenReturn(mocked);

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