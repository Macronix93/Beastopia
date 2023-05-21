package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

public class UserController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label username;

    @Inject
    public UserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }


    @FXML
    public void addRemove() {
    }

    @FXML
    public void pin() {
    }
}
