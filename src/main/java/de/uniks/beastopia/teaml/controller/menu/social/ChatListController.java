package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChatListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    private final List<Group> groups = new ArrayList<>();

    @Inject
    GroupListService groupListService;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    Provider<ChatUserController> chatUserControllerProvider;
    @Inject
    Provider<ChatGroupController> chatGroupControllerProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;
    @FXML
    private VBox chatList;
    private Consumer<Group> onGroupClicked;

    @Inject
    public ChatListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        reload();
        return parent;
    }

    public void reload() {
        disposables.add(groupListService.getGroups().observeOn(FX_SCHEDULER).subscribe(groups -> {
            this.groups.clear();
            this.groups.addAll(groups);
            chatList.getChildren().clear();
            updateGroupList();
        }));
    }

    public void setOnGroupClicked(Consumer<Group> onGroupClicked) {
        this.onGroupClicked = onGroupClicked;
    }

    private void addUser() {

    }

    private void createGroup() {

    }

    @Override
    public void destroy() {
        clearSubControllers();
        super.destroy();
    }

    @FXML
    public void showChats() {
        app.show(directMessageControllerProvider.get());
    }

    public void updateGroupList() {
        if (groups.size() == 0) {
            return;
        }

        clearSubControllers();

        // <ida>_<idb> is the name of a group with two members
        for (Group group : groups) {
            boolean groupPinned = prefs.isPinned(group);
            if (group.members().size() == 2 &&
                    groupListService.isSingleChat(group) &&
                    group.members().contains(tokenStorage.getCurrentUser()._id())) {
                ChatUserController chatUserController = chatUserControllerProvider.get();
                subControllers.add(chatUserController);
                chatUserController.setOnGroupClicked(onGroupClicked);
                chatUserController.setGroup(group, false);
                if (groupPinned) {
                    chatList.getChildren().add(0, chatUserController.render());
                } else {
                    chatList.getChildren().add(chatUserController.render());
                }
            } else {
                ChatGroupController chatGroupController = chatGroupControllerProvider.get();
                subControllers.add(chatGroupController);
                chatGroupController.setOnGroupClicked(onGroupClicked);
                chatGroupController.setGroup(group, false);
                if (groupPinned) {
                    chatList.getChildren().add(0, chatGroupController.render());
                } else {
                    chatList.getChildren().add(chatGroupController.render());
                }
            }
        }
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
    }

}
