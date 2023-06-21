package de.uniks.beastopia.teaml.controller.ingame;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeastControllerTest extends ApplicationTest {

    @Spy
    App app;
    @InjectMocks
    BeastController beastController;
    @Mock
    PresetsService presetsService;
    MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 0, 0, 0, attributes, currentAttributes);


    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        beastController.setBeast(monster);
        when(presetsService.getMonsterType(anyInt())).thenReturn(Observable.empty());
        when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.empty());

        app.start(stage);
        app.show(beastController);
        stage.requestFocus();
    }

    @Test
    void setBeastAndRenderTest() {
        assertEquals("HP: 0 / 1", lookup("#hp").queryAs(Label.class).getText());
        assertEquals("0", lookup("#level").queryAs(Label.class).getText());
    }
}