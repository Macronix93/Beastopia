package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class ChatWindowController extends Controller {

    private String namespace;
    private String parentId;
    private List<Message> messages = new ArrayList<>();
    @FXML
    public VBox msgList;

    @Inject
    Provider<MessageBubbleController> messageBubbleControllerProvider;

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
        if (namespace.equals("global")) {
            disposables.add(messageService.getMessagesFromFriend(parentId).observeOn(FX_SCHEDULER)
                    .subscribe(messages::addAll));
        } else if (namespace.equals("group")) {
            disposables.add(messageService.getMessagesFromGroup(parentId).observeOn(FX_SCHEDULER)
                    .subscribe(messageList -> {
                messages.addAll(messageList);
            }));
        }

        for (Message msg : messages) {
            Controller subController = messageBubbleControllerProvider.get().setupMessageBubbleController(msg);
            subControllers.add(subController);
            msgList.getChildren().add(subController.render());
        }

        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }
}
