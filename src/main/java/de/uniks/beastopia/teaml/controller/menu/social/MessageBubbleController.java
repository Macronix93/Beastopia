package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.sockets.EventListener;
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
    EventListener eventListener;

    @Inject
    FriendListService friendListService;

    Message message;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private Group group;

    @Inject
    public MessageBubbleController() {

    }

    public MessageBubbleController setMessage(Group group, Message message) {
        this.message = message;
        this.group = group;
        return this;
    }

    @Override
    public void init() {
        disposables.add(eventListener.listen("gruops." + group._id() + ".messages." + message._id() + ".updated", Message.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(this::updateMessage));
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        FriendListController.ALL_USERS.stream()
                .filter(user -> user._id().equals(message.sender()))
                .findFirst()
                .ifPresent(user ->
                        senderName.setText(user.name())
                );
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

    public void updateMessage(Event<Message> event) {
        Message message = event.data();
        messageBody.setText(message.body());
        LocalDateTime localDateTimeUpdated = LocalDateTime.ofInstant(message.createdAt().toInstant(), ZoneId.systemDefault());
        String updatedAt = localDateTimeUpdated.format(TIME_FORMAT);
        updated.setText(updatedAt);
    }

    @FXML
    public void editMessage() {
    }

    @FXML
    public void deleteMessage() {

    }

    public Message getMessage() {
        return message;
    }
}
