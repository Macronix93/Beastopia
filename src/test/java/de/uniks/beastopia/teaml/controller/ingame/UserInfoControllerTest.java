package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserInfoControllerTest extends ApplicationTest {
    @InjectMocks
    UserInfoController userInfoController;
    private final App app = new App();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        userInfoController
                .setAchievements(3)
                .setTotalAchievements(6)
                .setName("Leon");

        app.start(stage);
        app.show(userInfoController);
        stage.requestFocus();
    }

    @Test
    void render() {
        var node = lookup("3/6 Achievements").query();
        assertNotNull(node);
    }
}