package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTrainerControllerTest extends ApplicationTest {
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    @InjectMocks
    DeleteTrainerController deleteTrainerController;
    @Mock
    DataCache cache;
    @Mock
    Provider<TrainerController> trainerControllerProvider;
    @Mock
    TrainerService trainerService;

    final Trainer trainer = new Trainer(null, null, "123", "A", "123", "A", "A.png", null, List.of(), 0, null, 0, 0, 0, null);
    final TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    final List<HashMap<String, Double>> polygon = List.of(new HashMap<>() {{
        put("x", 0.0);
        put("y", 0.0);
    }});
    final List<HashMap<String, String>> properties = List.of(new HashMap<>() {{
        put("value", "AREA_NAME");
    }});
    final MapObject rectObject = new MapObject(50, 0, "AREA_NAME", properties, null, 0, "RECT", true, 50, 100, 100);
    final MapObject polyObject = new MapObject(50, 0, "POLY", null, polygon, 0, "POLY", true, 50, 60, 60);
    final Layer objectGroup = new Layer(null, List.of(), List.of(rectObject, polyObject), null, 1, 20, 20, "objectgroup", true, 2, 2, 0, 0);
    final Chunk chunk = new Chunk(List.of(0L, 1L, 2L, 3L), 2, 2, 0, 0);
    final Layer tilelayer = new Layer(List.of(chunk), List.of(), null, null, 1, 0, 0, "tilelayer", true, 2, 2, 0, 0);
    final Map map = new Map(List.of(tileSetDescription), List.of(tilelayer, objectGroup), 2, 24, 4);
    final Region region = new Region(null, null, "ID", "NAME", new Spawn(null, 0, 0), map);


    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(cache.getTrainer()).thenReturn(trainer);
        when(cache.getOrLoadTrainerImage(anyString(), anyBoolean())).thenReturn(Observable.just(new WritableImage(1, 1)));

        app.start(stage);
        app.show(deleteTrainerController);
        stage.requestFocus();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleTrainerDeletion"));
    }

    @Test
    void deleteTrainer() {
        when(cache.getJoinedRegion()).thenReturn(region);
        TrainerController mockedTrainerController = mock(TrainerController.class);
        when(trainerControllerProvider.get()).thenReturn(mockedTrainerController);
        doNothing().when(app).show(mockedTrainerController);

        when(trainerService.deleteTrainer(anyString(), anyString()))
                .thenReturn(Observable.just(trainer));

        clickOn("#trainerNameField");
        write("A");
        clickOn("#deleteTrainer");

        TextField trainerNameInput = lookup("#trainerNameField").query();
        assertEquals(trainer.name(), trainerNameInput.getText());

        verify(trainerService).deleteTrainer(anyString(), anyString());
        verify(app).show(mockedTrainerController);
    }

    @Test
    void cancel() {
        TrainerController mockedTrainerController = mock(TrainerController.class);
        when(trainerControllerProvider.get()).thenReturn(mockedTrainerController);
        doNothing().when(app).show(mockedTrainerController);
        when(mockedTrainerController.backController(any())).thenReturn(mockedTrainerController);

        clickOn("#cancel");

        verify(app).show(mockedTrainerController);
    }

    @Test
    void noTrainerNameEntered() {
        TextField trainerNameInput = lookup("#trainerNameField").query();
        assertEquals("", trainerNameInput.getText());

        clickOn("#deleteTrainer");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("enter your trainer name for confirmation")).query();
        assertNotNull(result);
    }

    @Test
    void trainerNameNotCorrect() {
        clickOn("#trainerNameField");
        write("B");

        TextField trainerNameInput = lookup("#trainerNameField").query();
        assertNotEquals(trainer.name(), trainerNameInput.getText());

        clickOn("#deleteTrainer");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("enter your trainer name for confirmation")).query();
        assertNotNull(result);
    }
}