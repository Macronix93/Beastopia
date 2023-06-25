package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ScoreboardFilterController extends Controller {
    @FXML
    private ScrollPane achievementList;
    @FXML
    private VBox achievementPane;

    @Inject
    public ScoreboardFilterController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        achievementPane.setSpacing(5);

        CheckBox checkBox = new CheckBox("test");
        achievementPane.getChildren().add(checkBox);

        achievementList.setContent(achievementPane);
        return parent;
    }
}
