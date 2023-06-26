package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class UserInfoController extends Controller {

    @FXML
    private Text nameText;
    @FXML
    private Text achievementsText;
    @FXML
    private ScrollPane achievementList;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private VBox achievementPane;

    private String name;
    private int achievements;
    private int totalAchievements;
    private List<Achievement> userAchievements;
    private Image userAvatar;

    @Inject
    DataCache cache;

    @Inject
    public UserInfoController() {
    }

    public UserInfoController setAchievements(int achievements) {
        this.achievements = achievements;
        return this;
    }

    public UserInfoController setTotalAchievements(int totalAchievements) {
        this.totalAchievements = totalAchievements;
        return this;
    }

    public UserInfoController setName(String name) {
        this.name = name;
        return this;
    }

    public UserInfoController setUserAchievements(List<Achievement> achievements) {
        this.userAchievements = new ArrayList<>(achievements);
        return this;
    }

    public UserInfoController setUserAvatar(Image avatar) {
        this.userAvatar = avatar;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        nameText.setText(name);
        achievementsText.setText(achievements + "/" + totalAchievements + " " + resources.getString("achievements"));
        avatarImageView.setImage(userAvatar);

        achievementPane.setSpacing(5);

        for (Achievement achievement : userAchievements) {
            Button button = new Button(cache.getAchievementDescriptions().get(achievement.id()));
            button.setPrefWidth(Double.MAX_VALUE);
            achievementPane.getChildren().add(button);
        }

        achievementList.setContent(achievementPane);
        return parent;
    }
}
