package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.FriendListService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    FriendListService friendListService;

    Message message;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @Inject
    public MessageBubbleController() {

    }

    public MessageBubbleController setMessage(Message message) {
        this.message = message;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(friendListService.getUser(this.message.sender()).observeOn(FX_SCHEDULER).subscribe(user -> this.senderName.setText(user.name())));
        messageBody.setText(message.body());
        LocalDateTime localDateTimeCreated = LocalDateTime.ofInstant(message.createdAt().toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTimeUpdated = LocalDateTime.ofInstant(message.createdAt().toInstant(), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = TIME_FORMAT;
        String createdAt = localDateTimeCreated.format(dateTimeFormatter);
        String updatedAt = localDateTimeUpdated.format(dateTimeFormatter);
        created.setText(createdAt);
        updated.setText(updatedAt);

        return parent;
    }

    @FXML
    public void editMessage() {

    }

    @FXML
    public void deleteMessage() {

    }
}
