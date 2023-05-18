package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.MainComponent;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.LoginResult;
import de.uniks.beastopia.teaml.service.AuthService;
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import retrofit2.HttpException;
import retrofit2.Response;

import javax.inject.Provider;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest extends ApplicationTest {

    @Mock
    Provider<RegistrationController> registrationControllerProvider;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Mock
    AuthService authService;
    @Mock
    @SuppressWarnings("unused")
    Prefs prefs;
    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    LoginController loginController;

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
        app.show(loginController);
        stage.requestFocus();
    }

    @Test
    void loginSuccessful() {
        LoginResult mocked = mock(LoginResult.class);
        when(authService.login(anyString(), anyString(), anyBoolean())).thenReturn(Observable.just(mocked));

        final MenuController mock = Mockito.mock(MenuController.class);
        when(menuControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        clickOn("#usernameInput");
        write("string");
        clickOn("#passwordInput");
        write("stringst");
        clickOn("#loginButton");

        verify(app).show(mock);
    }

    @Test
    void loginFail() {
        ResponseBody body = ResponseBody.create(MediaType.get("application/json"),
                "{\"message\":\"Login failed\"}");
        when(authService.login(anyString(), anyString(), anyBoolean())).thenReturn(Observable.error
                (new HttpException(Response.error(401, body))));
        clickOn("#usernameInput");
        write("string");
        clickOn("#passwordInput");
        write("12345678");
        clickOn("#loginButton");

        Node dialogPane = lookup(".dialog-pane").query();
        from(dialogPane).lookup((Text t) -> t.getText().startsWith("Login failed")).query();
    }

    @Test
    void register() {
        final RegistrationController mock = Mockito.mock(RegistrationController.class);
        when(registrationControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        clickOn("#registerButton");

        verify(app).show(mock);
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleLogin"));
    }
}
