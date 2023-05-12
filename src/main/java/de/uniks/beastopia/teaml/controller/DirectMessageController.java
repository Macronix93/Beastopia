package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.service.MessageService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class DirectMessageController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();

    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    MessageService messageService;
    @FXML
    public VBox chatList;
    @FXML
    public VBox messageList;
    @FXML
    public TextField chatInput;
    @FXML
    public Label chatName; //this label shows the name of the person/group you are chatting with

    @Inject
    public DirectMessageController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        //ToDo load Messages
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @Override
    public String getTitle() {
        return resources.getString("titleDirectMessages");
    }

    public void back() {
        app.show(menuControllerProvider.get());
    }

    public void newGroup() {
        //ToDo show Group Controller
    }

    public void sendMessage() {
        //ToDo send Message
    }
}
