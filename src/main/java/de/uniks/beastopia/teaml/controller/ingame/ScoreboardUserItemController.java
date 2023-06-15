package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.inject.Inject;
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

    @Override
    public Parent render() {
        Parent parent = super.render();

        nameText.setText(user.name());
        achievementsText.setText(achievements + "/" + totalAchievements + " Achievements");
        avatarImageView.setImage(cache.getImageAvatar(user));
        return parent;
    }

    public void toggleAchievements() {
        if (onUserClicked != null) {
            onUserClicked.accept(user._id());
        }
    }
}
