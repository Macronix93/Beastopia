package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.rest.UserAPIService;
import de.uniks.beastopia.teaml.service.FriendService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.List;

public class FriendListController extends Controller {
    @FXML
    public TextArea searchName;
    @FXML
    public Button searchBtn;
    @FXML
    public ScrollPane scrollFriends;
    @FXML
    public HBox friendList;
    @FXML
    public Button showChats;
    @Inject
    FriendService friendService;

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        final Parent parent = super.render();
        return parent;
    }

    @Override
    public void init() {
        User user = new User(); //TODO
        List<String> friendIds = null;
        for (User user1 : user.getFriends()) {
            friendIds.add(user1.getId());
        }
        friendService.getFriends(friendIds).subscribe(fl -> {
            //create new Friend in List
        });
    }

    public void showChats(ActionEvent actionEvent) {

    }


    public void searchUser(ActionEvent actionEvent) {
    }
}
