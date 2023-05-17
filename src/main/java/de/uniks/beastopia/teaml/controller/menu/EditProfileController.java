package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.prefs.Preferences;


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
    Preferences preferences;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<DeleteUserController> deleteUserControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    ThemeSettings themeSettings;
    @FXML
    private TextField usernameField;

    @Inject
    public EditProfileController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        usernameField.setText(tokenStorage.getCurrentUser().name());
        darkRadioButton.setSelected(preferences.getBoolean("DarkTheme", false));
        summerRadioButton.setSelected(!preferences.getBoolean("DarkTheme", false));
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditProfile");
    }

    public void uploadAvatar() {
    }

    public void setDarkTheme() {
        preferences.putBoolean("DarkTheme", true);
        themeSettings.updateSceneTheme.accept("dark");
    }

    public void setSummerTheme() {
        preferences.putBoolean("DarkTheme", false);
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

    public void backToMenu() {
        app.show(menuControllerProvider.get());
    }
}
