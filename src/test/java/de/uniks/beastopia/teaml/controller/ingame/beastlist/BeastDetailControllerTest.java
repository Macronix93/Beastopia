package de.uniks.beastopia.teaml.controller.ingame.beastlist;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeastDetailControllerTest extends ApplicationTest {

    final MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    final MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    final Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 0, 0, 0, Map.of("1", 1, "2", 2), attributes, currentAttributes);
    @Spy
    App app;
    @InjectMocks
    BeastDetailController beastDetailController;
    @Mock
    PresetsService presetsService;

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        beastDetailController.setBeast(monster);
        when(presetsService.getMonsterType(anyInt())).thenReturn(Observable.empty());
        when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.empty());

        app.start(stage);
        app.show(beastDetailController);
        stage.requestFocus();
    }

    @Test
    void setBeastAndRenderTest() {
        assertEquals("Level: 0", lookup("#level").queryAs(Label.class).getText());
        assertEquals("HP: 0 / 1", lookup("#hp").queryAs(Label.class).getText());
        assertEquals("Attack: 0", lookup("#attack").queryAs(Label.class).getText());
        assertEquals("Defense: 0", lookup("#defense").queryAs(Label.class).getText());
        assertEquals("Speed: 0", lookup("#speed").queryAs(Label.class).getText());
    }
}