package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class CreateGroupController extends Controller {

    public static final List<User> ALL_USERS = new ArrayList<>();
    @SuppressWarnings("unused")
    private final List<User> addedUsers = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public TextField usernameField;
    @FXML
    public VBox users;
    @FXML
    public TextField groupnameField;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    Provider<UserController> userControllerProvider;
    @Inject
    FriendListService friendListService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;

    @Inject
    public CreateGroupController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(friendListService.getUsers().subscribe(userList -> {
            ALL_USERS.clear();
            ALL_USERS.addAll(userList);
        }));

        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleNewGroup");
    }

    @FXML
    public void updateUserList() {
        clearSubControllers();
        if (usernameField.getText().isEmpty()) {
            return;
        }
        List<Parent> filteredParents = getFilteredParents();
        users.getChildren().addAll(filteredParents);
    }

    @FXML
    public void back() {
        app.show(directMessageControllerProvider.get());
    }

    public void createGroup() {


        /*
        // create new group
                      String groupName = groupListService.getGroupName(tokenStorage.getCurrentUser()._id(), user._id());
                        disposables.add(groupListService.addGroup(groupName, List.of(tokenStorage.getCurrentUser()._id(), user._id()))
                                .observeOn(FX_SCHEDULER)
                                .subscribe(group -> {
                                    chatListController.reload();
                                    loadGroup(group);
                                }));*/
    }

    @Override
    public void destroy() {
        super.destroy();
        clearSubControllers();
    }

    private void clearSubControllers() {
        subControllers.forEach(Controller::destroy);
        subControllers.clear();
        users.getChildren().clear();
    }

    private List<Parent> getFilteredParents() {

        List<User> filteredUsers = new ArrayList<>();
        List<Parent> filteredParents = new ArrayList<>();

        for (User user : ALL_USERS) {
            if (user.name().toLowerCase().startsWith(usernameField.getText().toLowerCase())
                    && !user._id().equals(tokenStorage.getCurrentUser()._id())) {
                filteredUsers.add(user);
            }
        }

        filteredUsers = filteredUsers.stream().sorted((firstUser, secondUser) -> {
            boolean notPinned = prefs.isPinned(firstUser);
            if (notPinned) {
                return -1;
            } else {
                return firstUser.name().compareTo(secondUser.name());
            }
        }).toList();


        for (User user : filteredUsers) {
            UserController userController = userControllerProvider.get();
            boolean pinned = prefs.isPinned(user);
            userController.setUser(user, pinned);
            userController.init();
            filteredParents.add(userController.render());
        }

        return filteredParents;
    }
}
