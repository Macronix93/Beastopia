package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class FriendListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<Controller>();
    private final List<User> allUsers = new ArrayList<User>();
    @FXML
    public TextField searchName;
    @FXML
    public Button searchBtn;
    @FXML
    public ScrollPane scrollFriends;
    @FXML
    public VBox friendList;
    @FXML
    public Button showChats;
    @Inject
    Provider<FriendController> friendControllerProvider;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    FriendListService friendListService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Preferences preferences;

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(friendListService.getUsers().subscribe(users -> {
            allUsers.clear();
            allUsers.addAll(users);
            searchUser();
        }));
        return parent;
    }

    private void getFriends() {
        disposables.add(friendListService.getFriends().observeOn(FX_SCHEDULER).subscribe(friends -> {
            clearSubControllers();
            if (friends != null) {
                for (User friend : friends) {
                    boolean friendPinned = preferences.getBoolean(friend._id() + "_pinned", true);
                    Controller subController = friendControllerProvider.get()
                            .setFriendController(friend, this, friendPinned);
                    subControllers.add(subController);
                    if (friendPinned) {
                        friendList.getChildren().add(0, subController.render());
                    } else {
                        friendList.getChildren().add(subController.render());
                    }
                }
            }
        }));
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
    public void searchUser() {
        if (searchName.getText().isEmpty()) {
            getFriends();
            return;
        }

        clearSubControllers();

        List<Parent> filteredParents = getFilteredParents();

        friendList.getChildren().addAll(filteredParents);
    }
    private List<Parent> getFilteredParents() {
        List<User> filteredUsers = new ArrayList<>();
        List<Parent> filteredParents = new ArrayList<>();

        for (User user : allUsers) {
            if (user.name().toLowerCase().startsWith(searchName.getText().toLowerCase())) {
                filteredUsers.add(user);
            }
        }

        filteredUsers.stream().sorted((firstUser, secondUser) -> {
            boolean notPinned = preferences.getBoolean(firstUser._id() + "_pinned", false);
            if (notPinned) {
                return -1;
            }
            else {
                return firstUser.name().compareTo(secondUser.name());
            }
        });

        for (User user : filteredUsers) {
            FriendController subController = friendControllerProvider.get();
            boolean friendPinned = preferences.getBoolean(user._id() + "_pinned", false);
            subController.setFriendController(user, this, friendPinned);
            filteredParents.add(subController.render());
        }
        return filteredParents;
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
        friendList.getChildren().clear();
    }
}
