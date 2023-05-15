package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class ChatListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    private final List<User> allUsers = new ArrayList<>();
    private final List<Group> allGroups = new ArrayList<>();

    @FXML
    private VBox chatList;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;

    @Inject
    public ChatListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
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

    @FXML
    public void updateUserList() {

        clearSubControllers();

        //add user to updated list
    }

    @FXML
    public void updateGroupList() {

        clearSubControllers();

        //add group to updated list
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
        //userList.getChildren().clear();
        //groupList.getChildren().clear();
    }

}
