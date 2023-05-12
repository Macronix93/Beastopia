package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<Controller>();
    private final List<User> allUsers = new ArrayList<User>();
    private final List<Group> allGroups = new ArrayList<Group>();


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
    }

}
