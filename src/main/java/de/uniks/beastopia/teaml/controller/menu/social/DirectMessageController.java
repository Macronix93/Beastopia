package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.MessageService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class DirectMessageController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();

    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    Provider<ChatWindowController> chatWindowControllerProvider;

    @Inject
    MessageService messageService;

    @Inject
    GroupListService groupListService;

    @Inject
    FriendListService friendListService;

    @Inject
    ChatListController chatListController;

    @FXML
    public GridPane grid;
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public TextField chatInput;
    @FXML
    public Label chatName; //this label shows the name of the person/group you are chatting with
    private String namespace;
    private String parentId;
    private Node rightSide;

    @Inject
    public DirectMessageController() {

    }

    public DirectMessageController setupDirectMessageController(String namespace, String parendId) {
        this.namespace = namespace;
        this.parentId = parendId;
        return this;
    }

    @Override
    public void init() {
        super.init();
        chatListController.setOnGroupClicked(group -> {
            grid.getChildren().remove(rightSide);
            rightSide = chatWindowControllerProvider.get().setupChatWindowController("group", group).render();
            grid.add(rightSide, 1, 1);
        });
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        rightSide = chatScrollPane;
        grid.add(chatListController.render(), 0, 1);

        //noinspection StatementWithEmptyBody
//        if (this.namespace == null || this.parentId == null) {
//            //TODO show first chat from ChatList
//        } else { //Open Chat from friend
//            if (this.namespace.equals("group")) {
//                disposables.add(groupListService.getGroup(parentId).observeOn(FX_SCHEDULER).subscribe(s -> chatName.setText(s.name())));
//            } else if (this.namespace.equals("global")) {
//                disposables.add(friendListService.getUser(parentId).observeOn(FX_SCHEDULER).subscribe(s -> chatName.setText(s.name())));
//            }
//            Controller subController = chatWindowControllerProvider.get()
//                    .setupChatWindowController(this.namespace, this.parentId);
//            subControllers.add(subController);
//
//        }
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @Override
    public String getTitle() {
        return resources.getString("titleDirectMessage");
    }

    public void back() {
        app.show(menuControllerProvider.get());
    }

    public void newGroup() {
        //ToDo show Group Controller
    }

    public void sendMessage() {
        //ToDo send Message
    }
}
