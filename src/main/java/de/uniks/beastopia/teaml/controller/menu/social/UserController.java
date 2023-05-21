package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class UserController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label username;

    private boolean pin;
    private User user;

    @Inject
    public UserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @Override
    public void init() {
        username.setText(user.name());
    }

    public UserController setUser(User user, boolean pin) {
        this.user = user;
        this.pin = pin;
        return this;
    }


    @FXML
    public void addRemove() {
    }

    @FXML
    public void pin() {
    }
}
