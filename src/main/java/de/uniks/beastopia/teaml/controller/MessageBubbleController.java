package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;
import java.util.Date;

public class MessageBubbleController extends Controller {
    @FXML
    public String senderName;
    @FXML
    public String messageBody;
    @FXML
    public Date created;
    @FXML
    public Date updated;
    @FXML
    public Button editButton;
    @FXML
    public Button deleteButton;

    @Inject
    public MessageBubbleController() {

    }

    public MessageBubbleController setupMessageBubbleController(Message message) {
        this.senderName = message.sender();
        this.messageBody = message.body();
        this.created = message.createdAt();
        this.updated = message.updatedAt();
        return this;
    }

    @FXML
    public void editMessage() {

    }

    @FXML
    public void deleteMessage() {

    }
}
