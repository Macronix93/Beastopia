package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class MessageBubbleController extends Controller {
    @FXML
    public Text senderName;
    @FXML
    public Text messageBody;
    @FXML
    public Text created;
    @FXML
    public Text updated;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;

    @Inject
    public MessageBubbleController() {

    }

    @FXML
    public void editMessage() {

    }

    @FXML
    public void deleteMessage() {

    }
}
