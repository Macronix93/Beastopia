package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreboardFilterController extends Controller {
    @FXML
    private ScrollPane achievementList;
    @FXML
    private VBox achievementPane;

    private List<AchievementsSummary> currentAchievements;
    private final List<Pair<CheckBox, AchievementsSummary>> checkBoxes = new ArrayList<>();
    private FilteredList<ScoreboardUserItemController> filteredControllers;
    private final List<String> selectedAchievements = new ArrayList<>();
    private Set<String> selectedAchievementSet;
    boolean filterApplied = false;
    VBox scoreBoard;

    @Inject
    DataCache cache;

    @Inject
    public ScoreboardFilterController() {
    }

    public ScoreboardFilterController setCurrentAchievements(List<AchievementsSummary> currentAchievements) {
        this.currentAchievements = new ArrayList<>(currentAchievements);
        return this;
    }

    public ScoreboardFilterController setSubControllers(ObservableList<ScoreboardUserItemController> subControllers) {
        filteredControllers = new FilteredList<>(subControllers, controller -> true);
        return this;
    }

    public ScoreboardFilterController setParentPane(VBox parent) {
        this.scoreBoard = parent;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        achievementPane.setSpacing(5);

        for (AchievementsSummary achievement : currentAchievements) {
            CheckBox checkBox = new CheckBox(cache.getAchievementDescriptions().get(achievement.id()));
            checkBox.setStyle("-fx-font-size: 14px;");
            checkBox.setOnAction(event -> {
                checkBoxes.add(new Pair<>(checkBox, achievement));
                applyFilter();
            });

            achievementPane.getChildren().add(checkBox);
        }

        for (int i = 0; i < 5; i++) {
            CheckBox checkBox = new CheckBox("Test " + i);
            checkBox.setStyle("-fx-font-size: 14px;");

            achievementPane.getChildren().add(checkBox);
        }

        achievementList.setContent(achievementPane);
        return parent;
    }

    public void applyFilter() {
        selectedAchievements.clear();

        for (Pair<CheckBox, AchievementsSummary> checkbox : checkBoxes) {
            if (checkbox.getKey().isSelected()) {
                selectedAchievements.add(checkbox.getValue().id());
            }
        }

        filterApplied = selectedAchievements.size() != 0;
        selectedAchievementSet = new HashSet<>(selectedAchievements);

        filteredControllers.setPredicate(controller -> {
            List<Achievement> achievements = controller.getUserAchievements();
            List<String> achievementIds = achievements.stream()
                    .map(Achievement::id)
                    .toList();
            return new HashSet<>(achievementIds).containsAll(selectedAchievementSet);
        });

        scoreBoard.getChildren().removeIf((Node node) -> {
            int index = scoreBoard.getChildren().indexOf(node);
            return index > 0;
        });

        for (ScoreboardUserItemController controller : filteredControllers) {
            scoreBoard.getChildren().add(controller.getParent());
        }
    }

    public void removeFilter() {
        for (Pair<CheckBox, AchievementsSummary> checkbox : checkBoxes) {
            if (checkbox.getKey().isSelected()) {
                checkbox.getKey().setSelected(false);
            }
        }

        filterApplied = false;
        filteredControllers.setPredicate(null);

        scoreBoard.getChildren().removeIf((Node node) -> {
            int index = scoreBoard.getChildren().indexOf(node);
            return index > 0;
        });

        for (ScoreboardUserItemController controller : filteredControllers) {
            scoreBoard.getChildren().add(controller.getParent());
        }
    }

    public boolean isFilterApplied() {
        return this.filterApplied;
    }

    public void checkCurrentController(ScoreboardUserItemController controller) {
        selectedAchievementSet = new HashSet<>(selectedAchievements);

        boolean hasControllerRequirements = filteredControllers.filtered(c ->
                new HashSet<>(c.getUserAchievements().stream()
                        .map(Achievement::id)
                        .toList())
                        .containsAll(selectedAchievementSet)
        ).contains(controller);

        if (hasControllerRequirements) {
            scoreBoard.getChildren().add(controller.getParent());
        }
    }
}
