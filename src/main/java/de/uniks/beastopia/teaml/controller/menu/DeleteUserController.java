package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class DeleteUserController extends Controller {

    @FXML
    public TextField usernameField;
    @FXML
    public TextField PasswordField;


    @Inject
    public DeleteUserController() {

    }


    @Override
    public String getTitle() {
        return resources.getString("titleDeleteUser");
    }

    public void deleteUser() {
    }

    public void cancel() {
    }
}

