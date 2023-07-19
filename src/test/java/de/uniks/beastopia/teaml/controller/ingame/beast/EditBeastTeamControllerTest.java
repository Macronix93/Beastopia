package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditBeastTeamControllerTest extends ApplicationTest {

    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    final ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @SuppressWarnings("unused")
    @Mock
    Prefs prefs;
    @InjectMocks
    EditBeastTeamController editBeastTeamController;
    @Mock
    TrainerService trainerService;
    @Mock
    PresetsService presetsService;
    @Mock
    DataCache cache;

    MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    Monster monster1 = new Monster(null, null, "MONSTER_1", "TRAINER_ID", 2, 1, 1, null,  attributes, currentAttributes);
    Monster monster2 = new Monster(null, null, "MONSTER_2", "TRAINER_ID", 4, 2, 5, null, attributes, currentAttributes);
    Monster monster3 = new Monster(null, null, "MONSTER_3", "TRAINER_ID", 0, 3, 2, null, attributes, currentAttributes);
    final Trainer trainer = new Trainer(null, null, "123", "A", "123", "A", "A.png", List.of("MONSTER_1"), List.of(), 0, null, 0, 0, 0, null);
    MonsterTypeDto monsterTypeDto = new MonsterTypeDto(0, "MONSTER_1", "MONSTER_TYPE.png", List.of(""), "");

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        when(cache.getTrainer()).thenReturn(trainer);
        when(trainerService.getTrainerMonsters(any(), any())).thenReturn(Observable.empty());
        when(presetsService.getAllBeasts()).thenReturn(Observable.just(List.of(monsterTypeDto)));
        doNothing().when(cache).setAllBeasts(any());

        app.start(stage);
        app.show(editBeastTeamController);
        stage.requestFocus();
        Platform.runLater(() -> this.editBeastTeamController.setupListView(List.of(monster1, monster2, monster3)));
    }

    @Test
    public void titleTest() {
        assertEquals(app.getStage().getTitle(), resources.getString("TitelBeastTeam"));
    }

    @Test
    public void renderTest() {
        ListView<Monster> beastListView = lookup("#beastListView").query();
        ListView<Monster> teamListView = lookup("#teamListView").query();

        assertEquals(2, beastListView.getItems().size());
        assertEquals(1, teamListView.getItems().size());
        assertNotNull(beastListView.getPlaceholder());
    }

    @Test
    public void backTest() {
        MenuController mocked = mock(MenuController.class);
        when(mocked.render()).thenReturn(new Label("backTest"));
        app.setHistory(List.of(mocked));
        clickOn("#beastTeamBack");
        verify(mocked, times(1)).render();
    }

    @Test
    public void selectAndSaveTest() {
        when(trainerService.updateTrainer(any(), any(), any(), any(), anyList())).thenReturn(Observable.just(trainer));
        MenuController mocked = mock(MenuController.class);
        when(mocked.render()).thenReturn(new Label("backTest"));
        app.setHistory(List.of(mocked));

        ListView<Monster> beastListView = lookup("#beastListView").query();
        ListView<Monster> teamListView = lookup("#teamListView").query();

        moveTo(app.getStage().getScene().getRoot(), new Point2D(-100, -100))
                .clickOn()
                .clickOn();

        assertEquals(0, beastListView.getItems().size());
        assertEquals(3, teamListView.getItems().size());

        moveTo(app.getStage().getScene().getRoot(), new Point2D(100, -150)).clickOn();

        assertEquals(1, beastListView.getItems().size());
        assertEquals(2, teamListView.getItems().size());

        clickOn("#editBeastTeam");
        verify(mocked, times(1)).render();
    }

    @Test
    public void removeTeamTest() {
        when(trainerService.updateTrainer(any(), any(), any(), any(), anyList())).thenReturn(Observable.just(trainer));
        MenuController mocked = mock(MenuController.class);
        when(mocked.render()).thenReturn(new Label("backTest"));
        app.setHistory(List.of(mocked));

        moveTo(app.getStage().getScene().getRoot(), new Point2D(100, -150)).clickOn();

        clickOn("#editBeastTeam");
        verify(cache, times(1)).setTrainer(any());
        verify(mocked, times(1)).render();
    }

    @Test
    public void startWithTeam() {

        ListView<Monster> beastListView = lookup("#beastListView").query();
        ListView<Monster> teamListView = lookup("#teamListView").query();

        assertEquals(2, beastListView.getItems().size());
        assertEquals(1, teamListView.getItems().size());
    }

    @Test
    public void filterLvlTest() {
        ListView<Monster> beastListView = lookup("#beastListView").query();
        assertEquals(2, beastListView.getItems().size());
        clickOn("#filterBar");
        write("2");
        assertEquals(1, beastListView.getItems().size());
    }

    @Test
    public void filterNameTest() {
        ListView<Monster> beastListView = lookup("#beastListView").query();
        assertEquals(2, beastListView.getItems().size());
        clickOn("#filterBar");
        write("MONSTER_1");
        assertEquals(1, beastListView.getItems().size());
    }


}