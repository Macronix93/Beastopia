package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ChatListController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();
    private final List<Group> groups = new ArrayList<>();
    @Inject
    GroupListService groupListService;
    @Inject
    Provider<ChatUserController> chatUserControllerProvider;
    @Inject
    Provider<ChatGroupController> chatGroupControllerProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;
    @Inject
    EventListener eventListener;
    @FXML
    private VBox chatList;
    private Consumer<Group> onGroupClicked;
    private boolean firstRender = true;

    @Inject
    public ChatListController() {

    }

    public void preventFirstUpdate() {
        firstRender = false;
    }

    @Override
    public void init() {
        disposables.add(eventListener.listen("groups.*.*", Group.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(group -> reload()));
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        reload();
        return parent;
    }

    public void reload() {
        if (chatList == null) {
            return;
        }
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

    @Override
    public void destroy() {
        clearSubControllers();
        super.destroy();
    }

    public void updateGroupList() {
        if (groups.size() == 0) {
            return;
        }

        clearSubControllers();
        chatList.getChildren().clear();

        // <ida>_<idb> is the name of a group with two members
        HashMap<Parent, Group> groupMap = new HashMap<>();
        for (Group group : groups) {
            boolean groupPinned = prefs.isPinned(group);
            if (group.members().size() == 2 &&
                    groupListService.isSingleChat(group) &&
                    group.members().contains(tokenStorage.getCurrentUser()._id())) {
                createUserController(group, groupPinned, groupMap);
            } else {
                createGroupController(group, groupPinned, groupMap);
            }
        }

        if (firstRender && chatList.getChildren().size() > 0) {
            firstRender = false;
            //noinspection SuspiciousMethodCalls
            onGroupClicked.accept(groupMap.get(chatList.getChildren().get(0)));
        }
    }

    private void createGroupController(Group group, boolean groupPinned, HashMap<Parent, Group> groupMap) {
        ChatGroupController chatGroupController = chatGroupControllerProvider.get();
        subControllers.add(chatGroupController);
        chatGroupController.setOnGroupClicked(onGroupClicked);
        chatGroupController.setGroup(group);
        chatGroupController.init();
        chatGroupController.setOnPinChanged(e -> updateGroupList());
        Parent parent = chatGroupController.render();
        if (groupPinned) {
            chatList.getChildren().add(0, parent);
        } else {
            chatList.getChildren().add(parent);
        }
        groupMap.put(parent, group);
    }

    private void createUserController(Group group, boolean groupPinned, HashMap<Parent, Group> groupMap) {
        ChatUserController chatUserController = chatUserControllerProvider.get();
        subControllers.add(chatUserController);
        chatUserController.setOnGroupClicked(onGroupClicked);
        chatUserController.setGroup(group);
        chatUserController.init();
        chatUserController.setOnPinChanged(e -> updateGroupList());
        Parent parent = chatUserController.render();
        if (groupPinned) {
            chatList.getChildren().add(0, parent);
        } else {
            chatList.getChildren().add(parent);
        }
        groupMap.put(parent, group);
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
    }
}
