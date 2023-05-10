package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatWindowController extends Controller{

    private String namespace;
    private String parentId;
    @FXML
    public ScrollPane scrollMsg;
    @FXML
    public VBox msgList;
    @FXML
    public TextArea msgText;
    @FXML
    public Button sendButton;

    //@Inject
    //Provider<MessageController> messageControllerProvider;

    @Inject
    MessageService messageService;

    private final List<Controller> subControllers = new ArrayList<Controller>();

    @Inject
    public ChatWindowController() {

    }

    public ChatWindowController setupChatWindowController(String namespace, String parendId) {
        this.namespace = namespace;
        this.parentId = parendId;
        return this;
    }


    @Override
    public Parent render() {
        Parent parent = super.render();
        List<Message> messages = new ArrayList<>();
        if (namespace.equals("global")) {
            disposables.add(messageService.getMessagesFromFriend(parentId).observeOn(FX_SCHEDULER)
                    .subscribe(messages::addAll));
        } else if (namespace.equals("group")) {
            disposables.add(messageService.getMessagesFromGroup(parentId).observeOn(FX_SCHEDULER)
                    .subscribe(messages::addAll));
        }

        for (Message msg : messages) {
            //Controller subController = messageControllerProvider.get().setupMessageController(msg);
            //subControllers.add(subController);
            //msgList.getChildren().add(subController)
        }


        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        //Display text from TextArea "msgTxt" into a Message (subcontroller)
    }
}
