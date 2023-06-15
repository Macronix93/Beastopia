package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class MessageBubbleController extends Controller {
    @FXML
    public Text senderName;
    @FXML
    public Text messageBody;
    @FXML
    public TextField editMessageBody;
    @FXML
    public VBox elementsBox;
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
    MessageService messageService;
    @Inject
    DataCache cache;
    Consumer<Pair<Parent, MessageBubbleController>> onDelete;
    Message message;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private Group group;
    Parent parent;
    boolean editMode = false;

    @Inject
    public MessageBubbleController() {

    }

    public MessageBubbleController setOnDelete(Consumer<Pair<Parent, MessageBubbleController>> onDelete) {
        this.onDelete = onDelete;
        return this;
    }

    public MessageBubbleController setMessage(Group group, Message message) {
        this.message = message;
        this.group = group;
        return this;
    }

    @Override
    public void init() {
        disposables.add(eventListener.listen("groups." + group._id() + ".messages." + message._id() + ".updated", Message.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(this::updateMessage));
    }

    @Override
    public Parent render() {
        parent = super.render();

        elementsBox.getChildren().remove(editMessageBody);

        if (!messageService.isSentByMe(message)) {
            editButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        cache.getAllUsers().stream().filter(user -> user._id().equals(message.sender())).findFirst().ifPresent(user -> senderName.setText(user.name()));
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
        if (editMode) {
            return;
        }
        elementsBox.getChildren().add(1, editMessageBody);
        elementsBox.getChildren().remove(messageBody);
        editMessageBody.setText(message.body());
        editMode = true;
    }

    @FXML
    public void keyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            if (!editMode) {
                return;
            }

            elementsBox.getChildren().add(1, messageBody);
            elementsBox.getChildren().remove(editMessageBody);
            event.consume();
            editMode = false;
        } else if (event.getCode().equals(KeyCode.ENTER)) {
            if (!editMode) {
                return;
            }

            disposables.add(messageService.updateMessage(group, message, editMessageBody.getText()).observeOn(FX_SCHEDULER).subscribe(message -> {
                this.message = message;
                messageBody.setText(message.body());
                elementsBox.getChildren().add(1, messageBody);
                elementsBox.getChildren().remove(editMessageBody);
                editMode = false;
            }, throwable -> Dialog.error(throwable, "Problem while updating message")));

            event.consume();
        }
    }

    @FXML
    public void deleteMessage() {
        if (editMode) {
            return;
        }

        disposables.add(messageService.deleteMessage(group, message)
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        msg -> onDelete.accept(new Pair<>(parent, this)),
                        error -> Dialog.error(error, "Problem while deleting message")));
    }
}
