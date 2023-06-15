package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.util.ResourceBundle;

public class KeybindElementController extends Controller {

    private String key;
    private String action;
    @FXML
    public Button keyBtn;
    @FXML
    public Label keyLabel;
    @Inject
    ResourceBundle resources;

    @Inject
    public KeybindElementController() {
    }

    public void setKeyAndAction (String key, String action) {
        this.key = key;
        this.action = action;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        keyBtn.setText(key);
        keyLabel.setText(resources.getString(action));
        return parent;
    }

    @FXML
    @SuppressWarnings("EmptyMethod")
    public void changeKey() {
        //ToDo change key
    }
}
