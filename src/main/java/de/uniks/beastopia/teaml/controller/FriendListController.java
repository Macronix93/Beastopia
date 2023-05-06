package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.service.RefreshService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.UserService;
import javafx.application.Platform;
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
    RefreshService refreshService;

    @Inject
    UserService userService;
    @Inject
    TokenStorage tokenStorage;

    private final List<Controller> subControllers = new ArrayList<Controller>();

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(refreshService.refresh(tokenStorage.getRefreshToken()).subscribe(r -> {
            for (String friendId : r.friends()) {
                disposables.add(userService.getUser(friendId).subscribe(f -> {
                    Platform.runLater(() -> {
                        Controller subController = friendControllerProvider.get().setUser(f);
                        subControllers.add(subController);
                        friendList.getChildren().add(subController.render());
                    });
                }));
            }
        }));
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
    }

    @FXML
    public void showChats(ActionEvent actionEvent) {

    }

    @FXML
    public void searchUser(ActionEvent actionEvent) {
    }
}
