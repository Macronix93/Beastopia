package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.service.LoginService;
import de.uniks.beastopia.teaml.service.RefreshService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

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
    RefreshService refreshService;
    @Inject
    TokenStorage tokenStorage;

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        return super.render();
    }

    @Override
    public void init() {
        refreshService.refresh(tokenStorage.getRefreshToken()).subscribe(r -> {
            User currentUser = new User()
                    .setAvatar(r.getAvatar())
                    .setId(r.getId())
                    .withFriends(r.getFriends())
                    .setName(r.getName())
                    .setStatus(r.getStatus())
                    .setCreatedAt(r.getCreatedAt())
                    .setUpdatedAt(r.getUpdatedAt());
            friendList.getChildren().add(new FriendController().render());
            //TODO das richtig
        });
    }

    @FXML
    public void showChats(ActionEvent actionEvent) {

    }

    @FXML
    public void searchUser(ActionEvent actionEvent) {
    }
}
