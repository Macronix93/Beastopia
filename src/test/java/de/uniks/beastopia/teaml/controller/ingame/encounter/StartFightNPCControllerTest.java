package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.glassfish.grizzly.utils.ObjectPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Inject;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartFightNPCControllerTest extends ApplicationTest {

    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @InjectMocks
    StartFightNPCController startFightNPCController;
    @Mock
    Prefs prefs;
    @Mock
    TrainerService trainerService;
    @Mock
    DataCache cache;
    @Mock
    EncounterOpponentsService encounterOpponentsService;
    @Mock
    PresetsService presetsService;

    Encounter encounter = new Encounter(null, null, "ID", "r", false);
    Opponent opponent = new Opponent(null, null, "ido", "e",
            "t", true, true, "m", null, null, 0);

    Trainer trainer = new Trainer(null, null, "tid", "tr", "tu", "tn",
            "ti", null, 0, "tarea", 2, 3, 0, null);
    List<Opponent> ops = List.of(opponent);

    Region region = new Region(null, null, "id", "Alb", null, null);
    Image expectedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/bt_icon.png")));

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        startFightNPCController.setControllerInfo(encounter);
        when(encounterOpponentsService.getEncounterOpponents(any(), any()))
                .thenReturn(Observable.just(ops));
        when(cache.getTrainer()).thenReturn(trainer);
        when(cache.getJoinedRegion()).thenReturn(region);
        when(trainerService.getTrainer(any(), any())).thenReturn(Observable.just(trainer));
        when(presetsService.getCharacterSprites(any(), eq(true))).thenReturn(Observable.just(expectedImage));

        app.start(stage);
        app.show(startFightNPCController);
        stage.requestFocus();
    }

    @Test
    void setScreen() {
        assertEquals(expectedImage, lookup("#image").queryAs(ImageView.class).getImage());
        assertEquals(trainer.name() + " " + resources.getString("npcStart"), lookup("#headline")
                .queryAs(Label.class).getText());
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }

    @Test
    void startFight() {
        //TODO when encounter-screen ready
    }
}