package de.uniks.beastopia.teaml.controller.ingame.beastlist;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.AssetProvider;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeastControllerTest extends ApplicationTest {

    final MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    AssetProvider assets;
    @InjectMocks
    BeastController beastController;
    @Mock
    PresetsService presetsService;
    @Mock
    DataCache cache;
    final MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    final Monster monster = new Monster(null, null, "MONSTER_ID", "TRAINER_ID", 0, 0, 0, null, attributes, currentAttributes, List.of("burned"));
    final MonsterTypeDto monsterTypeDto = new MonsterTypeDto(1, "name", "image", List.of("1", "2"), "a");
    final Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/group.png")));

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        beastController.setBeast(monster);
        when(cache.getBeastDto(anyInt())).thenReturn(monsterTypeDto);
        when(presetsService.getMonsterImage(anyInt())).thenReturn(Observable.just(image));

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