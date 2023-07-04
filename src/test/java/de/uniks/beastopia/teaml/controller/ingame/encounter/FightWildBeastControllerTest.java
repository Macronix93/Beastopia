package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
    @Mock
    PresetsService presetsService;

    @Mock
    TrainerService trainerService;

    @Mock
    Prefs prefs;

    Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 1, 0,
            0, null, null);

    MonsterTypeDto monsterTypeDto = new MonsterTypeDto(3, "name", "image", null, null);

    Image expectedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/bt_icon.png")));
    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        fightWildBeastController.setControllerInfo("beast", "trainer");
        when(prefs.getRegionID()).thenReturn("region");
        when(trainerService.getTrainerMonster("region", "trainer", "beast"))
                .thenReturn(Observable.just(monster));
        when(presetsService.getMonsterType(anyInt()))
                .thenReturn(Observable.just(monsterTypeDto));
        when(presetsService.getMonsterImage(anyInt()))
                .thenReturn(Observable.just(expectedImage));
        when(prefs.getLocale()).thenReturn("en");

        app.start(stage);
        app.show(fightWildBeastController);
        stage.requestFocus();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }

    @Test
    void setScreen() {
        assertEquals(expectedImage, lookup("#image").queryAs(ImageView.class).getImage());
        assertEquals("A wild " + monsterTypeDto.name() + " appears!", lookup("#headline").queryAs(Label.class).getText());
    }

    @Test
    void startFight() {
        //TODO show EndScreen
        //following just to undo the fight, that the server knows

    }
}