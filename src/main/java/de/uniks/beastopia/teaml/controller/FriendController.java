package de.uniks.beastopia.teaml.controller;


import de.uniks.beastopia.teaml.rest.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.prefs.Preferences;

public class FriendController extends Controller {
    @FXML
    public ImageView friendAvatar;
    @FXML
    public Circle statusCircle;
    @FXML
    public Text name;
    @FXML
    public Button action;
    @FXML
    public Button chat;
    @FXML
    public Button pin;

    public HBox _rootElement;

    private User user;
    private FriendListController friendListController;

    private Boolean friendPin;

    private final ImageView pinned = createImage("de/uniks/beastopia/teaml/assets/buttons/filled_pin.png");

    private final ImageView notPinned = createImage("de/uniks/beastopia/teaml/assets/buttons/pin.png");
    @Inject
    Preferences preferences;

    @Inject
    public FriendController() {

    }

    public FriendController setFriendController(User user, FriendListController friendListController, boolean friendPin) {
        this.user = user;
        this.friendListController = friendListController;
        this.friendPin = friendPin;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        //TODO change avatar URL when avatar upload is implemented to individual link
        Image image = new Image("de/uniks/beastopia/teaml/assets/Lumnix_Logo_tr.png", 40.0,
                40.0, false, false);
        friendAvatar.setImage(image);

        name.setText(user.name());

        if (user.status().equals("online")) {
            statusCircle.setFill(Paint.valueOf("green"));
        } else {
            statusCircle.setFill(Paint.valueOf("red"));
        }

        if (this.friendPin) {
            this.pin.setGraphic(pinned);
        } else {
            this.pin.setGraphic(notPinned);
        }

        return parent;
    }

    private ImageView createImage(String imageUrl) {
        ImageView imageView = new ImageView(new Image(imageUrl));
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }

    @FXML
    public void editFriendList(ActionEvent actionEvent) {
    }

    @FXML
    public void openFriendChat(ActionEvent actionEvent) {
    }

    @FXML
    public void pinFriend(ActionEvent actionEvent) {
        if (pin.getGraphic() == notPinned) {
            friendListController.friendList.getChildren().remove(_rootElement);
            friendListController.friendList.getChildren().add(0, this.render());
            pin.setGraphic(pinned);
            preferences.putBoolean(this.user._id() + "_pinned", true);
        } else {
            pin.setGraphic(notPinned);
            preferences.putBoolean(this.user._id() + "_pinned", false);
        }
    }
}
