package de.uniks.beastopia.teaml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DirectMessageController extends Controller{

    private final List<Controller> subControllers = new ArrayList<Controller>();
    @FXML
    public VBox chatList;
    @FXML
    public VBox messageList;
    @FXML
    public TextField chatInput;

    @Inject
    public DirectMessageController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();


        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @Override
    public String getTitle() {
        return resources.getString("titleDirectMessage");
    }

    public void back() {
    }

    public void newGroup() {
    }

    public void sendMessage() {
    }
}
