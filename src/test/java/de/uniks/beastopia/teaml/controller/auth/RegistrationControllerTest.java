package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.RegistrationService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
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
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest extends ApplicationTest {

    @Mock
    RegistrationService registrationService;

    @Mock
    Provider<LoginController> loginControllerProvider;

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @Spy
    App app;

    @InjectMocks
    RegistrationController registrationController;

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        app.show(registrationController);
        stage.requestFocus();
    }

    @Test
    void signUpSuccessful() {
        User mocked = mock(User.class);
        when(registrationService.createUser(anyString(), anyString(), anyString())).thenReturn(Observable.just(mocked));

        clickOn("#usernameInput");
        write("Lonartie");
        clickOn("#passwordInput");
        write("12345678");
        clickOn("#passwordRepeatInput");
        write("12345678");

        clickOn("#signUpButton");

        Node dialogPane = lookup(".dialog-pane").query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("Registration successful")).query();

        verify(registrationService).createUser(eq("Lonartie"), any(), eq("12345678"));
    }

    @Test
    void signUpWrongPasswords() {
        clickOn("#usernameInput");
        write("Lonartie");
        clickOn("#passwordInput");
        write("1234567");
        clickOn("#passwordRepeatInput");
        write("12345678");

        clickOn("#signUpButton");

        Node dialogPane = lookup(".dialog-pane").query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("Passwords do not match")).query();
    }

    @Test
    void signUpWrongAlreadyExists() {
        ResponseBody body = ResponseBody.create(MediaType.get("application/json"), "{\"message\":\"User already exists\"}");
        when(registrationService.createUser(anyString(), anyString(), anyString())).thenReturn(Observable.error(new HttpException(Response.error(409, body))));

        clickOn("#usernameInput");
        write("Lonartie");
        clickOn("#passwordInput");
        write("12345678");
        clickOn("#passwordRepeatInput");
        write("12345678");

        clickOn("#signUpButton");

        Node dialogPane = lookup(".dialog-pane").query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("Registration failed")).query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("User already exists")).query();
    }

    @Test
    void switchToSignIn() {
        final LoginController mocked = mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(mocked);
        doNothing().when(app).show(mocked);

        clickOn("#loginButton");

        verify(app).show(mocked);
    }
}