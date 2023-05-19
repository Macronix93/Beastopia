package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class EditProfileController extends Controller {

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
    private TextField usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public PasswordField passwordRepeatInput;

    @Inject
    Provider<ResourceBundle> resourcesProvider;
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

    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty passwordRepeat = new SimpleStringProperty();

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

        String userLocale = Locale.getDefault().toLanguageTag();
        if (!userLocale.equals("en-EN") && !userLocale.equals("de-DE")) {
            userLocale = "en-EN";
        }

        if (prefs.get("locale", userLocale).contains("de")) {
            selectGermanLanguage.setSelected(true);
        } else {
            selectEnglishLanguage.setSelected(true);
        }
        usernameInput.textProperty().bindBidirectional(username);
        passwordInput.textProperty().bindBidirectional(password);
        passwordRepeatInput.textProperty().bindBidirectional(passwordRepeat);

        usernameInput.setText(tokenStorage.getCurrentUser().name());
        darkRadioButton.setSelected(prefs.getBoolean("DarkTheme", false));
        summerRadioButton.setSelected(!prefs.getBoolean("DarkTheme", false));
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
        prefs.setTheme("dark");
        themeSettings.updateSceneTheme.accept("summer");
    }

    public void changePassword() {
        if (!passwordInput.getText().isEmpty()) {
            setNewPassword();
        }
    }

    private void setNewPassword() {
        if (!passwordInput.getText().equals(passwordRepeatInput.getText())) {
            errorMessage("passwordsNotEqual");
        } else if (passwordInput.getText().length() < 8) {
            errorMessage("passwordTooShort");
        } else {
            disposables.add(authService.updatePassword(passwordInput.getText())
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

    public void setDe() {
        setLanguage(Locale.GERMAN);
    }

    public void setEn() {
        setLanguage(Locale.ENGLISH);
    }

    private void setLanguage(Locale locale) {
        prefs.put("locale", locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
    }

}
