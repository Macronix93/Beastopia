package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScoreboardUserItemControllerTest extends ApplicationTest {

    @InjectMocks
    ScoreboardUserItemController scoreboardUserItemController;


    private final App app = new App();
    private final Consumer<String> onUserClicked = Mockito.mock();

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);
        doNothing().when(onUserClicked).accept(Mockito.anyString());

        scoreboardUserItemController
                .setOnUserClicked(onUserClicked)
                .setAchievements(3)
                .setUserId("123")
                .setTotalAchievements(6)
                .setName("Leon");

        app.start(stage);
        app.show(scoreboardUserItemController);
        stage.requestFocus();
    }

    @Test
    void render() {
        var node = lookup("3/6 Achievements").query();
        assertNotNull(node);
    }

    @Test
    void toggleAchievements() {
        var node = lookup("3/6 Achievements").query();
        assertNotNull(node);

        clickOn(node);

        verify(onUserClicked).accept("123");
    }
}