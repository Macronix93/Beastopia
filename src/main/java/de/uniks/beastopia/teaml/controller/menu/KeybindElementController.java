package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;

public class KeybindElementController extends Controller {

    @FXML
    public Button keyBtn;
    @FXML
    public Label keyLabel;

    @Inject
    public KeybindElementController() {
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void changeKey() {

    }
}
