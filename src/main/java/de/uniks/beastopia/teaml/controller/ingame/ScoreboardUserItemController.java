package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.function.Consumer;

public class ScoreboardUserItemController extends Controller {

    @FXML
    private Text nameText;
    @FXML
    private Text achievementsText;
    private String name;
    private String userId;
    private int achievements;
    private int totalAchievements;
    Consumer<String> onUserClicked;

    @Inject
    public ScoreboardUserItemController() {
    }

    public ScoreboardUserItemController setOnUserClicked(Consumer<String> onUserClicked) {
        this.onUserClicked = onUserClicked;
        return this;
    }

    public ScoreboardUserItemController setAchievements(int achievements) {
        this.achievements = achievements;
        return this;
    }

    public ScoreboardUserItemController setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ScoreboardUserItemController setTotalAchievements(int totalAchievements) {
        this.totalAchievements = totalAchievements;
        return this;
    }

    public ScoreboardUserItemController setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        nameText.setText(name);
        achievementsText.setText(achievements + "/" + totalAchievements + " Achievements");

        // TODO: load avatar
        return parent;
    }

    public void toggleAchievements() {
        if (onUserClicked != null) {
            onUserClicked.accept(userId);
        }
    }
}
