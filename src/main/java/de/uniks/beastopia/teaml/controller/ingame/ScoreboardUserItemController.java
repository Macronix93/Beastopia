package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScoreboardUserItemController extends Controller {

    @FXML
    private Text nameText;
    @FXML
    private Text achievementsText;
    @FXML
    private ImageView avatarImageView;
    @Inject
    DataCache cache;
    private User user;
    private int achievements;
    private int totalAchievements;
    Consumer<String> onUserClicked;
    private List<Achievement> userAchievements;
    private Parent parent;

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

    public ScoreboardUserItemController setUser(User user) {
        this.user = user;
        return this;
    }

    public ScoreboardUserItemController setTotalAchievements(int totalAchievements) {
        this.totalAchievements = totalAchievements;
        return this;
    }

    public ScoreboardUserItemController setUserAchievements(List<Achievement> achievements) {
        this.userAchievements = new ArrayList<>(achievements);
        return this;
    }

    public List<Achievement> getUserAchievements() {
        return this.userAchievements;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return this.parent;
    }

    @Override
    public Parent render() {
        parent = super.render();

        nameText.setText(user.name());
        achievementsText.setText(achievements + "/" + totalAchievements + " " + resources.getString("achievements"));
        avatarImageView.setImage(cache.getImageAvatar(user));
        return parent;
    }

    public void toggleAchievements() {
        if (onUserClicked != null) {
            onUserClicked.accept(user._id());
        }
    }
}
