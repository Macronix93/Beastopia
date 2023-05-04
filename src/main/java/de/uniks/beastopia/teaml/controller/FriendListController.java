package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.model.User;
import de.uniks.beastopia.teaml.service.LoginService;
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
    LoginService loginService;

    @Inject
    public FriendListController() {

    }

    @Override
    public Parent render() {
        return super.render();
    }

    @Override
    public void init() {
        loginService.login("string", "stringst").subscribe(lr -> {
            System.out.println(lr.getFriends());
            //Get friends
            //friend subcontroller
        });
        User user = new User();
        //get friends from user
        //for every fiend
        for (User friend : user.getFriends()) {
            //Friend
            friendList.getChildren().add(new FriendController().render());
            //Braucht man FriendService oder hat man die Liste schon so?
        }
    }

    @FXML
    public void showChats(ActionEvent actionEvent) {

    }

    @FXML
    public void searchUser(ActionEvent actionEvent) {
    }
}
