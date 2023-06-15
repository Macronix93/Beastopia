package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserControllerTest extends ApplicationTest {

    @Spy
    App app;
    @InjectMocks
    DeleteUserController deleteUserController;
    @Mock
    AuthService authService;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    Provider<EditProfileController> editProfileControllerProvider;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    final User user = new User(null, null, "1", "1", null, null, null);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(tokenStorage.getCurrentUser()).thenReturn(user);


        app.start(stage);
        app.show(deleteUserController);
        stage.requestFocus();
    }

    @Test
    public void deleteUser() {
        clickOn("#passwordField");
        write("12345678");
        clickOn("#deleteUserButton");
        verify(authService, times(1)).login(any(), any(), anyBoolean());
    }


    @Test
    public void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleDeleteUser"));
    }

}