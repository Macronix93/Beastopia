package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class MessageBubbleController extends Controller {
    @FXML
    public Text usernameText;
    @FXML
    public Text messageText;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;

    @Inject
    public MessageBubbleController() {

    }

    @Override
    public String getTitle() {
        return "Beastopia";
    }

    @Override
    public Parent render() {
        return null;
    }

    @FXML
    public void editMessage() {

    }

    public void deleteMessage() {

    }
}
