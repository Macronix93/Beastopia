package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;


public class EditProfileController extends Controller {

    private final SimpleStringProperty username = new SimpleStringProperty("");
    private final SimpleStringProperty password = new SimpleStringProperty("");
    private final SimpleStringProperty passwordRepeat = new SimpleStringProperty("");
    @FXML
    public RadioButton darkRadioButton;
    @FXML
    public RadioButton summerRadioButton;
    @FXML
    public ToggleGroup language;
    @FXML
    public ToggleGroup theme;
    @FXML
    public RadioButton selectEnglishLanguage;
    @FXML
    public RadioButton selectGermanLanguage;
    @FXML
    public Button editProfileButton;
    @FXML
    public Button deleteUserButton;
    @FXML
    public Button backButton;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public PasswordField passwordRepeatInput;
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
    @FXML
    private TextField usernameInput;
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
        usernameInput.textProperty().bindBidirectional(username);
        passwordInput.textProperty().bindBidirectional(password);
        passwordRepeatInput.textProperty().bindBidirectional(passwordRepeat);
        usernameInput.setText(tokenStorage.getCurrentUser().name());
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditProfile");
    }

    public void uploadAvatar() {
    }

    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resources.getString("error"));
        alert.setHeaderText(resources.getString("error"));
        alert.setContentText(resources.getString(message));
        alert.showAndWait();
    }

    public void deleteUser() {
        app.show(deleteUserControllerProvider.get().backController(backController));
    }

    public void back() {
        if (this.backController.equals("menu")) {
            app.show(menuControllerProvider.get());
        } else {
            app.show(pauseControllerProvider.get());
        }

    }

    @FXML
    public void editProfile() {
        if (usernameInput.getText().isEmpty()) {
            errorMessage("usernameEmpty");
        } else if (!passwordInput.getText().isBlank()) {
            if (usernameInput.getText().equals(tokenStorage.getCurrentUser().name())) {
                setNewPassword();
            } else {
                setNewUsernameAndPassword();
            }
        } else if (!usernameInput.getText().equals(tokenStorage.getCurrentUser().name())) {
            setNewUsername();
        }
    }

    private void setNewUsername() {
        disposables.add(authService.updateUsername(usernameInput.getText())
                .observeOn(FX_SCHEDULER).subscribe(
                        lr -> app.show(menuControllerProvider.get()),
                        error -> Dialog.error(error, resources.getString("usernameChangeFailed"))));
    }

    private void setNewPassword() {
        if (!passwordInput.getText().equals(passwordRepeatInput.getText())) {
            errorMessage("passwordsNotEqual");
        } else {
            disposables.add(authService.updatePassword(passwordInput.getText())
                    .observeOn(FX_SCHEDULER).subscribe(
                            lr -> app.show(menuControllerProvider.get()),
                            error -> Dialog.error(error, resources.getString("passwordChangeFailed"))));
        }
    }

    private void setNewUsernameAndPassword() {
        if (!passwordInput.getText().equals(passwordRepeatInput.getText())) {
            errorMessage("passwordsNotEqual");
        } else {
            disposables.add(authService.updateUsernameAndPassword(usernameInput.getText(), passwordInput.getText())
                    .observeOn(FX_SCHEDULER).subscribe(
                            lr -> app.show(menuControllerProvider.get()),
                            error -> Dialog.error(error, resources.getString("UsernameAndPasswordChangeFailed"))));
        }
    }

}
