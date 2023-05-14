package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.GroupListService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<Controller>();
    private final List<User> allUsers = new ArrayList<User>();
    private final List<Group> allGroups = new ArrayList<Group>();

    @FXML
    private VBox chatList;
    @Inject
    FriendListService friendListService;
    @Inject
    GroupListService groupListService;

    @Inject
    public ChatListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    private void getFriends() {

    }

    private void getGroups() {

    }

    @Override
    public void destroy() {
        clearSubControllers();
        super.destroy();
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
        //friendList.getChildren().clear();
    }

}
