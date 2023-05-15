package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteUserController extends Controller {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Provider<EditProfileController> editProfileControllerProvider;

    @Inject
    public DeleteUserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        usernameField.setText(tokenStorage.getCurrentUser().name());
        return parent;
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

