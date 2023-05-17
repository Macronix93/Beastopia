package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
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
    private final List<Message> messages = new ArrayList<>();
    @FXML
    public VBox msgList;

    @Inject
    Provider<MessageBubbleController> messageBubbleControllerProvider;

    @Inject
    MessageService messageService;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Controller> subControllers = new ArrayList<>();

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
                    .subscribe(this::fillInMessages));
        } else if (namespace.equals("group")) {
            disposables.add(messageService.getMessagesFromGroup(parentId).observeOn(FX_SCHEDULER)
                    .subscribe(this::fillInMessages));
        }

        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    private void fillInMessages(List<Message> msgs) {
        messages.clear();
        messages.addAll(msgs);

        for (Message msg : messages) {
            Controller subController = messageBubbleControllerProvider.get().setMessage(msg);
            subControllers.add(subController);
            msgList.getChildren().add(subController.render());
        }
    }
}
