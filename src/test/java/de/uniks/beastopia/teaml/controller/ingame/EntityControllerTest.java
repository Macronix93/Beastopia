package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.PlayerState;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityControllerTest extends ApplicationTest {

    @SuppressWarnings("unused")
    @Mock
    PresetsService presetsService;
    @Mock
    DataCache cache;
    @InjectMocks
    EntityController entityController;
    @Spy
    App app;

    final ObjectProperty<PlayerState> state = new SimpleObjectProperty<>(PlayerState.IDLE);
    final Trainer trainer = new Trainer(null, null, "ID_TRAINER", "ID_REGION", "ID_USER", "TRAINER_NAME", "TRAINER_IMAGE", null, List.of(), 0, "ID_AREA", 0, 0, 0, new NPCInfo(false, false, false, false, List.of(), List.of(), List.of()));

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(cache.getOrLoadTrainerImage(anyString(), anyBoolean())).thenReturn(Observable.just(new WritableImage(1, 1)));
        entityController.setTrainer(trainer);
        entityController.playerState().bind(state);
        app.start(stage);
        app.show(entityController);
        stage.requestFocus();
    }

    @Test
    void render() {
        assertNotNull(lookup("#entityView"));
    }
}