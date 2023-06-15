package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.image.BufferedImage;
import java.io.File;


public class EditProfileController extends Controller {

    private final SimpleStringProperty username = new SimpleStringProperty("");
    private final SimpleStringProperty password = new SimpleStringProperty("");
    private final SimpleStringProperty passwordRepeat = new SimpleStringProperty("");
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
    @FXML
    public ImageView avatarPreview;
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
    DataCache cache;
    @FXML
    private TextField usernameInput;
    private String backController;
    private BufferedImage bufferedImage = null;

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
        avatarPreview.setImage(cache.getImageAvatar(tokenStorage.getCurrentUser()));
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
        File file = cache.provideImageFile(app);
        this.bufferedImage = cache.provideBufferedImage(file);
        avatarPreview.setImage(new Image(file.toURI().toString(), 128, 128, true, true, true));
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

        if (bufferedImage != null) {
            setNewAvatar();
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

    private void setNewAvatar() {
        disposables.add(authService.updateAvatar(cache.getAvatarDataUrl(bufferedImage)).observeOn(FX_SCHEDULER).subscribe(
                lr -> app.show(menuControllerProvider.get()),
                error -> Dialog.error(error, resources.getString("avatarChangeFailed"))));
    }

    @Override
    public void destroy() {
        super.destroy();
        avatarPreview = null;
    }
}
