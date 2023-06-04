package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class DirectMessageController extends Controller {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Controller> subControllers = new ArrayList<>();

    @FXML
    public Button backButton;

    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    Provider<ChatWindowController> chatWindowControllerProvider;
    @Inject
    Provider<EditGroupController> editGroupControllerProvider;
    @Inject
    Provider<CreateGroupController> createGroupControllerProvider;
    @Inject
    MessageService messageService;

    @Inject
    GroupListService groupListService;

    @Inject
    ChatListController chatListController;
    @Inject
    TokenStorage tokenStorage;

    @FXML
    public GridPane grid;
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public TextField chatInput;
    @FXML
    public Label chatName; //this label shows the name of the person/group you are chatting with
    private Node rightSide;
    private Group currentGroup;

    @Inject
    public DirectMessageController() {

    }

    public DirectMessageController setupDirectMessageController(User user) {
        // check if group already exists
        disposables.add(groupListService.getGroups()
                .observeOn(FX_SCHEDULER)
                .subscribe(groups -> {
                    if (groups == null) {
                        return;
                    }
                    //noinspection ReassignedVariable
                    Group existing = null;
                    for (Group group : groups) {
                        if (group.members().size() == 2 &&
                                group.members().contains(user._id()) &&
                                group.members().contains(tokenStorage.getCurrentUser()._id())) {
                            existing = group;
                            break;
                        }
                    }

                    if (existing != null) {
                        loadGroup(existing);
                    } else {
                        // create new group
                        String groupName = groupListService.getGroupName(tokenStorage.getCurrentUser()._id(), user._id());
                        disposables.add(groupListService.addGroup(groupName, List.of(tokenStorage.getCurrentUser()._id(), user._id()))
                                .observeOn(FX_SCHEDULER)
                                .subscribe(group -> {
                                    if (group == null) {
                                        return;
                                    }
                                    chatListController.reload();
                                    loadGroup(group);
                                }));
                    }
                }));

        return this;
    }

    @Override
    public void init() {
        super.init();
        chatListController.setOnGroupClicked(this::loadGroup);
        chatListController.init();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        rightSide = chatScrollPane;
        grid.add(chatListController.render(), 0, 1);

        //TODO: show chatBox label
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

    @FXML
    public void createGroup() {
        app.show(createGroupControllerProvider.get());
    }

    public void sendMessage() {
        if (currentGroup == null || chatInput.getText().isBlank()) {
            return;
        }

        String message = chatInput.getText();
        disposables.add(messageService.sendMessageToGroup(currentGroup, message).subscribe(r -> chatInput.setText("")));
    }

    private void loadGroup(Group group) {
        currentGroup = group;
        grid.getChildren().remove(rightSide);
        ChatWindowController controller = chatWindowControllerProvider.get().setupChatWindowController(group);
        controller.init();
        rightSide = controller.render();
        grid.add(rightSide, 1, 1);
    }
}
