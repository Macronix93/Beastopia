package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
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
    Provider<ChatWindowController> chatWindowControllerProvider;
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
    @Inject
    DataCache cache;
    @FXML
    public GridPane grid;
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    public TextField chatInput;
    @FXML
    public Label chatNameLabel;
    private Node rightSide;
    private Group currentGroup;
    ChatWindowController controller;
    private boolean preventDefaultGroupLoading = false;

    @Inject
    public DirectMessageController() {

    }

    public DirectMessageController setupDirectMessageController(User user) {
        preventDefaultGroupLoading = true;

        // check if group already exists
        disposables.add(groupListService.getGroups()
                .observeOn(FX_SCHEDULER)
                .subscribe(groups -> {
                    if (groups == null) {
                        return;
                    }
                    Group existing = getExistingGroup(groups, user);
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

    private Group getExistingGroup(List<Group> groups, User user) {
        for (Group group : groups) {
            if (group.members().size() == 2 &&
                    group.members().contains(user._id()) &&
                    group.members().contains(tokenStorage.getCurrentUser()._id())) {
                return group;
            }
        }
        return null;
    }

    @Override
    public void init() {
        super.init();
        chatListController.setOnGroupClicked(this::loadGroup);
        if (preventDefaultGroupLoading) {
            chatListController.preventFirstUpdate();
        }
        chatListController.init();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        rightSide = chatScrollPane;
        grid.add(chatListController.render(), 0, 1);

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
        app.showPrevious();
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
        disposables.add(messageService
                .sendMessageToGroup(currentGroup, message)
                .observeOn(FX_SCHEDULER)
                .subscribe(r -> {
                            if (r == null) {
                                return;
                            }
                            chatInput.setText("");
                        }
                ));
    }

    private void loadGroup(Group group) {
        currentGroup = group;
        grid.getChildren().remove(rightSide);
        if (controller != null) {
            controller.destroy();
        }
        controller = chatWindowControllerProvider.get().setupChatWindowController(group);

        if (!groupListService.isSingleChat(group)) {
            chatNameLabel.setText(group.name());
        } else {
            List<String> memberList = group.members();
            if (memberList.get(0).equals(tokenStorage.getCurrentUser()._id())) {
                chatNameLabel.setText(cache.getUser(memberList.get(1)).name());
            } else {
                chatNameLabel.setText(cache.getUser(memberList.get(0)).name());
            }
        }

        controller.init();
        rightSide = controller.render();
        grid.add(rightSide, 1, 1);
    }
}
