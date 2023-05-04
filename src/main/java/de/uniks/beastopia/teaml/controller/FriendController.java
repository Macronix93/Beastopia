package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;

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

    private User user;

    @Inject
    public FriendController(User friend) {
        this.user = friend;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        friendAvatar.setImage(new Image(user.getAvatar()));

        name.setText(user.getName());

        if (user.getStatus().equals("online")) {
            statusCircle.setFill(Paint.valueOf("green"));
        } else {
            statusCircle.setFill(Paint.valueOf("red"));
        }

        return parent;
    }

    @FXML
    public void editFriendList(ActionEvent actionEvent) {
    }

    @FXML
    public void openFriendChat(ActionEvent actionEvent) {
    }

    @FXML
    public void pinFriend(ActionEvent actionEvent) {
    }
}
