package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FightWildBeastControllerTest extends ApplicationTest {

    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @InjectMocks
    FightWildBeastController fightWildBeastController;


    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        app.start(stage);
        app.show(fightWildBeastController);
        stage.requestFocus();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }

    @Test
    void startFight() {
    }
}