package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class EditProfileController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    public TextField newPasswordField;
    @FXML
    public TextField retypeNewPasswordField;


    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    TokenStorage tokenStorage;

    @Inject
    public EditProfileController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        usernameField.setText(tokenStorage.getCurrentUser().name());
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditProfile");
    }

    public void uploadAvatar() {
    }

    public void editProfile() {
    }

    public void deleteUser() {
    }

    public void backToMenu() {
        app.show(menuControllerProvider.get());
    }
}
