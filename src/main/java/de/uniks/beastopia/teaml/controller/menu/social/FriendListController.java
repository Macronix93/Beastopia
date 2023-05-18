package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
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

public class FriendListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    private final List<User> allUsers = new ArrayList<>();
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
    Prefs prefs;

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(friendListService.getUsers().subscribe(users -> {
            allUsers.clear();
            allUsers.addAll(users);
            updateUserList();
        }));
        return parent;
    }

    private void getFriends() {
        disposables.add(friendListService.getFriends().observeOn(FX_SCHEDULER).subscribe(friends -> {
            clearSubControllers();
            if (friends != null) {
                for (User friend : friends) {
                    boolean friendPinned = prefs.isPinned(friend);
                    FriendController friendController = friendControllerProvider.get()
                            .setUser(friend, friendPinned);
                    friendController.init();
                    subControllers.add(friendController);
                    friendController.setOnFriendChanged(user -> {
                        searchName.setText("");
                        updateUserList();
                    });
                    friendController.setOnPinChanged(user -> updateUserList());
                    if (friendPinned) {
                        friendList.getChildren().add(0, friendController.render());
                    } else {
                        friendList.getChildren().add(friendController.render());
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
    public void updateUserList() {
        if (searchName.getText().isBlank()) {
            getFriends();
            return;
        }

        clearSubControllers();

        List<Parent> filteredParents = getFilteredParents();
        friendList.getChildren().addAll(filteredParents);
    }

    private List<Parent> getFilteredParents() {
        @SuppressWarnings("ReassignedVariable")
        List<User> filteredUsers = new ArrayList<>();
        List<Parent> filteredParents = new ArrayList<>();

        for (User user : allUsers) {
            if (user.name().toLowerCase().startsWith(searchName.getText().toLowerCase())
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
            FriendController friendController = friendControllerProvider.get();
            friendController.init();
            friendController.setOnFriendChanged(user_ -> {
                searchName.setText("");
                updateUserList();
            });
            friendController.setOnPinChanged(user_ -> updateUserList());
            boolean friendPinned = prefs.isPinned(user);
            friendController.setUser(user, friendPinned);
            filteredParents.add(friendController.render());
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
