package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.GroupListService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    private final List<User> allUsers = new ArrayList<>();
    private final List<Group> allGroups = new ArrayList<>();

    @FXML
    private VBox chatList;
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

    private void getUsers() {

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
        //userList.getChildren().clear();
    }

}
