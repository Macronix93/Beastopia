package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.service.MessageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChatWindowController extends Controller{

    @FXML
    public ScrollPane scrollMsg;
    @FXML
    public VBox msgList;
    @FXML
    public TextArea msgText;
    @FXML
    public Button sendButton;

    //@Inject
    //Provider<MessageController> messageControllerProvider;

    @Inject
    MessageService messageService;

    private final List<Controller> subControllers = new ArrayList<Controller>();

    @Inject
    public ChatWindowController() {

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

    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        //Display text from TextArea "msgTxt" into a Message (subcontroller)
    }
}
