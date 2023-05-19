package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;


public class EditProfileController extends Controller {

    @FXML
    public PasswordField newPasswordField;
    @FXML
    public PasswordField retypeNewPasswordField;
    @FXML
    public RadioButton darkRadioButton;
    @FXML
    public RadioButton summerRadioButton;
    @FXML
    public ToggleGroup language;
    @FXML
    public ToggleGroup theme;
    @Inject
    Prefs prefs;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<DeleteUserController> deleteUserControllerProvider;

    @Inject
    Provider<PauseController> pauseControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    ThemeSettings themeSettings;
    @FXML
    private TextField usernameField;
    private String backController;


    @Inject
    public EditProfileController() {

    }

    public EditProfileController backController(String controller) {
        this.backController = controller;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        usernameField.setText(tokenStorage.getCurrentUser().name());
        darkRadioButton.setSelected(prefs.getTheme().equals("dark"));
        summerRadioButton.setSelected(!prefs.getTheme().equals("dark"));
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditProfile");
    }

    public void uploadAvatar() {
    }

    public void setDarkTheme() {
        prefs.setTheme("dark");
        themeSettings.updateSceneTheme.accept("dark");
    }

    public void setSummerTheme() {
        prefs.setTheme("summer");
        themeSettings.updateSceneTheme.accept("summer");
    }

    public void changePassword() {
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

    public void back() {
        if (this.backController.equals("menu")) {
            app.show(menuControllerProvider.get());
        } else {
            app.show(pauseControllerProvider.get());
        }

    }
}
