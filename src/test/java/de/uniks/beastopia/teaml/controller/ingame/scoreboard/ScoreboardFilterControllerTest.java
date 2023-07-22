package de.uniks.beastopia.teaml.controller.ingame.scoreboard;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.ingame.scoreboard.ScoreboardFilterController;
import de.uniks.beastopia.teaml.controller.ingame.scoreboard.ScoreboardUserItemController;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreboardFilterControllerTest extends ApplicationTest {
    @Mock
    DataCache cache;
    @InjectMocks
    ScoreboardFilterController scoreboardFilterController;
    private final App app = new App();
    private final List<AchievementsSummary> achievementSummaries = List.of(
            new AchievementsSummary("ID1", 0, 2, 3),
            new AchievementsSummary("ID2", 0, 2, 3)
    );
    private final Map<String, String> achievementDescription = new HashMap<>();
    private List<CheckBox> currentCheckBoxes;
    private final ObservableList<ScoreboardUserItemController> subControllers = FXCollections.observableArrayList();
    private final List<Achievement> achievements = List.of(
            new Achievement(null, null, "ID1", "ID", new Date(), 0),
            new Achievement(null, null, "ID2", "ID", new Date(), 0)
    );
    @Mock
    private VBox scoreBoard;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        currentCheckBoxes = List.of(new CheckBox("check1"), new CheckBox("check2"));
        achievementDescription.put("ID1", "ID1_Description");
        achievementDescription.put("ID2", "ID2_Description");

        ScoreboardUserItemController controller = new ScoreboardUserItemController();
        subControllers.add(controller);

        scoreboardFilterController
                .setParentPane(scoreBoard)
                .setCurrentAchievements(achievementSummaries)
                .setSubControllers(subControllers);

        when(cache.getAchievementDescriptions()).thenReturn(achievementDescription);

        app.start(stage);
        app.show(scoreboardFilterController);
        stage.requestFocus();
    }

    @Test
    void removeFilter() throws NoSuchFieldException, IllegalAccessException {
        ObservableList<ScoreboardUserItemController> filteredControllers = new FilteredList<>(subControllers, controller -> true);
        scoreboardFilterController.setSubControllers(filteredControllers);
        setPrivateField(scoreboardFilterController, filteredControllers);

        ObservableList<CheckBox> children = FXCollections.observableArrayList(currentCheckBoxes.get(0), currentCheckBoxes.get(1));
        doReturn(children).when(scoreBoard).getChildren();

        scoreboardFilterController.removeFilter();

        assertNotNull(getPrivateField(scoreboardFilterController));
    }

    @Test
    void applyFilter() {
        ObservableList<Node> childrenList = FXCollections.observableArrayList();
        when(scoreBoard.getChildren()).thenReturn(childrenList);

        ScoreboardUserItemController mockController = mock(ScoreboardUserItemController.class);
        when(mockController.getUserAchievements()).thenReturn(achievements);

        scoreboardFilterController.setSubControllers(FXCollections.observableArrayList(mockController));
        scoreboardFilterController.applyFilter();

        verify(scoreBoard, times(2)).getChildren();
    }

    @Test
    void checkCurrentController() {
        ScoreboardUserItemController mockController = mock(ScoreboardUserItemController.class);

        scoreboardFilterController.setCurrentAchievements(achievementSummaries);

        VBox scoreBoard = new VBox();
        scoreboardFilterController.setParentPane(scoreBoard);

        ScoreboardUserItemController controller1 = mock(ScoreboardUserItemController.class);
        when(controller1.getUserAchievements()).thenReturn(List.of(achievements.get(0)));
        ScoreboardUserItemController controller2 = mock(ScoreboardUserItemController.class);
        when(controller2.getUserAchievements()).thenReturn(List.of(achievements.get(1)));
        List<ScoreboardUserItemController> filteredControllersList = new ArrayList<>();
        filteredControllersList.add(controller1);
        filteredControllersList.add(controller2);
        FilteredList<ScoreboardUserItemController> filteredControllers = new FilteredList<>(FXCollections.observableArrayList(filteredControllersList), controller -> true);
        scoreboardFilterController.setSubControllers(filteredControllers);

        scoreboardFilterController.checkCurrentController(mockController);

        assertFalse(scoreBoard.getChildren().contains(mockController.getParent()));
    }

    private void setPrivateField(Object targetObject, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField("filteredControllers");
        field.setAccessible(true);
        field.set(targetObject, value);
    }

    private Object getPrivateField(Object targetObject) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetObject.getClass().getDeclaredField("filteredControllers");
        field.setAccessible(true);
        return field.get(targetObject);
    }
}
