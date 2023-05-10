package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class FriendListController extends Controller {
    @FXML
    public TextArea searchName;
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
    FriendListService friendListService;
    @Inject
    TokenStorage tokenStorage;

    @Inject
    Preferences preferences;

    private final List<Controller> subControllers = new ArrayList<Controller>();

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        FriendListController friendListController = this;
        disposables.add(friendListService.getFriends().observeOn(FX_SCHEDULER).subscribe(friends -> {
            if (friends != null) {
                for (User friend : friends) {
                    boolean friendPinned = preferences.getBoolean(friend._id() + "_pinned", true);
                    Controller subController = friendControllerProvider.get()
                            .setFriendController(friend, friendListController, friendPinned);
                    subControllers.add(subController);
                    if (friendPinned) {
                        friendList.getChildren().add(0, subController.render());
                    } else {
                        friendList.getChildren().add(subController.render());
                    }
                }
            }
        }));

        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @FXML
    public void showChats(ActionEvent actionEvent) {

    }

    @FXML
    public void searchUser(ActionEvent actionEvent) {
    }
}
