package de.uniks.beastopia.teaml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
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

    @Inject
    public FriendController() {
    }


    public void editFriendList(ActionEvent actionEvent) {
    }

    public void openFriendChat(ActionEvent actionEvent) {
    }

    public void pinFriend(ActionEvent actionEvent) {
    }
}
