package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import io.reactivex.rxjava3.core.Observable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class ChatWindowController extends Controller {

    private final List<Message> messages = new ArrayList<>();
    @Inject
    EventListener eventListener;
    @FXML
    public VBox msgList;
    @Inject
    Provider<MessageBubbleController> messageBubbleControllerProvider;
    @Inject
    MessageService messageService;
    private final List<MessageBubbleController> subControllers = new ArrayList<>();
    private Group group;
    private LoadingPage loadingPage;

    @Inject
    public ChatWindowController() {

    }

    public ChatWindowController setupChatWindowController(Group group) {
        this.group = group;
        return this;
    }

    @Override
    public void init() {
        super.init();
        disposables.add(eventListener.listen("groups." + group._id() + ".messages.*.created", Message.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(this::addMessage));
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        Observable<List<Message>> messagesFromGroup = messageService.getMessagesFromGroup(group._id());
        if (messagesFromGroup != null) {
            disposables.add(messagesFromGroup
                    .observeOn(FX_SCHEDULER)
                    .subscribe(this::fillInMessages));
        }

        return loadingPage.parent();
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    private void addMessage(Event<Message> event) {
        addMessageData(event.data());
    }

    private void addMessageData(Message message) {
        MessageBubbleController subController = messageBubbleControllerProvider.get()
                .setMessage(group, message)
                .setOnDelete(pair -> {
                    Parent parent = pair.getKey();
                    MessageBubbleController controller = pair.getValue();
                    msgList.getChildren().remove(parent);
                    subControllers.remove(controller);
                });
        subController.init();
        subControllers.add(subController);
        Parent chatBubble = subController.render();
        VBox box = new VBox();
        box.getChildren().add(chatBubble);

        if (messageService.isSentByMe(message)) {
            box.setAlignment(Pos.TOP_RIGHT);
        } else {
            box.setAlignment(Pos.TOP_LEFT);
        }

        msgList.getChildren().add(box);
    }

    private void fillInMessages(List<Message> msgs) {
        messages.clear();
        messages.addAll(msgs);
        messages.forEach(this::addMessageData);
        loadingPage.setDone();
    }
}
