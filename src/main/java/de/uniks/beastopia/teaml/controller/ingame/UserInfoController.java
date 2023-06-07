package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AchievementsService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;

public class UserInfoController extends Controller {

    @FXML
    private Text nameText;
    @FXML
    private Text achievementsText;
    @FXML
    private VBox achievements;
    @Inject
    DataCache cache;
    @Inject
    AchievementsService achievementsService;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;
    @Inject
    Provider<ScoreboardController> scoreboardControllerProvider;
    private String name;

    //private int achievements;
    private int totalAchievements;


    public UserInfoController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }
}
