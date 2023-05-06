package de.uniks.beastopia.teaml.controller;


import de.uniks.beastopia.teaml.rest.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.imageio.stream.FileImageInputStream;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.FileInputStream;

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

    private ImageView notPinned;
    private ImageView pinned;

    @Inject
    public FriendController() {

    }

    public FriendController setUserConttroller(User user, FriendListController friendListController) {
        this.user = user;
        this.friendListController = friendListController;
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

        this.notPinned = new ImageView("de/uniks/beastopia/teaml/assets/buttons/pin.png");
        this.notPinned.setFitHeight(25.0);
        this.notPinned.setFitWidth(25.0);
        this.pinned = new ImageView("de/uniks/beastopia/teaml/assets/buttons/filled_pin.png");
        this.pinned.setFitHeight(25.0);
        this.pinned.setFitWidth(25.0);

        pin.setGraphic(notPinned);

        return parent;
    }

    @FXML
    public void editFriendList(ActionEvent actionEvent) {
        //Icons for methods in assets\buttons
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
        } else {
            pin.setGraphic(notPinned);
        }
    }
}
