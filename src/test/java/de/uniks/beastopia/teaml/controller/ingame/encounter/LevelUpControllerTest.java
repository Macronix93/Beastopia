package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.image.Image;
import io.reactivex.rxjava3.core.Observable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LevelUpControllerTest extends ApplicationTest {

    @InjectMocks
    LevelUpController levelUpController;
    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang",
            Locale.forLanguageTag("en"));
    @Mock
    PresetsService presetsService;

    @Mock
    Prefs prefs;

    Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 1, 0,
            0, null, null, null);
    Image expectedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/bt_icon.png")));
    MonsterTypeDto monsterTypeDto = new MonsterTypeDto(3, "name", "image", null, null);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        levelUpController.setBeast(monster, true, true , 2); //other scenarios don't have more tests
        when(presetsService.getMonsterType(any())).thenReturn(Observable.just(monsterTypeDto));
        when(presetsService.getMonsterImage(any()))
                .thenReturn(Observable.just(expectedImage));
        when(presetsService.getAbility(any())).thenReturn(Observable.just(new AbilityDto(1, "a", "b",
                "fire", 1, 1, 1)));
        when(prefs.getLocale()).thenReturn("en");

        app.start(stage);
        app.show(levelUpController);
        stage.requestFocus();
    }

    @Test
    void setScreen() {

    }

    @Test
    void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }

    @Test
    void continuePressed() {
    }
}