package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;


public class EditProfileController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public PasswordField retypeNewPasswordField;


    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<DeleteUserController> deleteUserControllerProvider;
    @Inject
    AuthService authService;
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
        if (!newPasswordField.getText().isEmpty()) {
            setNewPassword();
        }
    }

    private void setNewPassword() {
        if (!newPasswordField.getText().equals(retypeNewPasswordField.getText())) {
            errorMessage("passwordsNotEqual");
        } else if (newPasswordField.getText().length() < 8) {
            errorMessage("passwordTooShort");
        } else {
            disposables.add(authService.updatePassword(newPasswordField.getText())
                    .observeOn(FX_SCHEDULER).subscribe(
                            lr -> app.show(menuControllerProvider.get()),
                            error -> Dialog.error(error, resources.getString("passwordChangeFailed"))));
        }
    }

    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error"));
        alert.setHeaderText(resources.getString("error"));
        alert.setContentText(resources.getString(message));
        alert.showAndWait();
    }

    public void deleteUser() {
        app.show(deleteUserControllerProvider.get());
    }

    public void backToMenu() {
        app.show(menuControllerProvider.get());
    }
}
