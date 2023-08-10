package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LevelUpControllerTest extends ApplicationTest {

    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang",
            Locale.forLanguageTag("en"));
    final Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 10, 0,
            0, Map.of("1", 1, "2", 2), new MonsterAttributes(1, 1, 1, 1),
            new MonsterAttributes(1, 1, 1, 1), null);
    final Image expectedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/group.png")));
    final MonsterTypeDto monsterTypeDto = new MonsterTypeDto(3, "name", "image", List.of("1", "2"), "a");
    final Map<String, Integer> abilities = new HashMap<>() {{
        put("1", 1);
    }};
    final AbilityDto abilityDto = new AbilityDto(1, "name", "description", "type", 1, 1, 1);
    @InjectMocks
    LevelUpController levelUpController;
    @Spy
    App app;
    @Mock
    DataCache cache;
    @Mock
    PresetsService presetsService;

    @Override
    public void start(Stage stage) throws InterruptedException {
        AppPreparer.prepare(app);

        when(cache.getBeastDto(anyInt())).thenReturn(monsterTypeDto);
        when(cache.getMonsterImage(anyInt())).thenReturn(expectedImage);
        when(presetsService.getAbility(anyInt())).thenReturn(Observable.just(abilityDto));
        when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.just(expectedImage));
        levelUpController.setBeast(monster, abilities, true, 2, 2, 2, 2, null); // other scenarios don't have more tests

        app.start(stage);
        app.show(levelUpController);
        stage.requestFocus();

        Thread.sleep(2000);
    }

    @Test
    void setScreen() {
        assertEquals(expectedImage, lookup("#image").queryAs(ImageView.class).getImage());
    }

    @Test
    void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }
}