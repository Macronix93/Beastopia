package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppTest extends ApplicationTest {
    private Stage stage;
    @Mock
    MainComponent mainComponent;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        App app = new App(mainComponent);
        MainComponent realMainComponent = DaggerMainComponent.builder().mainApp(app).build();
        realMainComponent.prefs().setLocale("en");

        Prefs mockedPrefs = mock();
        when(mockedPrefs.getLocale()).thenReturn("en");
        when(mockedPrefs.isRememberMe()).thenReturn(false);
        when(mockedPrefs.getTheme()).thenReturn("dark");
        when(mainComponent.loginController()).thenAnswer(i -> realMainComponent.loginController());
        when(mainComponent.themeSettings()).thenAnswer(i -> realMainComponent.themeSettings());
        when(mainComponent.prefs()).thenReturn(mockedPrefs);

        app.start(stage);
        stage.requestFocus();
    }

    @Test
    void initialScreenIsLogin() {
        assertEquals("Beastopia - Login", stage.getTitle());
    }

    @Test
    void canSwitchBetweenLoginAndRegistration() {
        assertEquals("Beastopia - Login", stage.getTitle());
        clickOn("#registerButton");
        assertEquals("Beastopia - Registration", stage.getTitle());
        clickOn("#loginButton");
        assertEquals("Beastopia - Login", stage.getTitle());
    }
}