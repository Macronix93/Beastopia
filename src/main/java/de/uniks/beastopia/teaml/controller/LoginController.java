package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController extends Controller {
    @FXML
    public TextField usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Button loginButton;
    @FXML
    public RadioButton selectEnglishLanguage;
    @FXML
    public RadioButton selectGermanLanguage;
    @Inject
    Provider<RegistrationController> registrationControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Preferences preferences;
    @Inject
    Provider<ResourceBundle> resourcesProvider;
    private BooleanBinding isInValid;

    @Inject
    public LoginController() {

    }

    @Override
    public String getTitle() {
        return "Beastopia - Login";
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        isInValid = usernameInput.textProperty().isEmpty()
                .or(passwordInput.textProperty().length().lessThan(8));
        loginButton.disableProperty().bind(isInValid);

        return parent;
    }

    @FXML
    public void login() {
        if (isInValid.get()) {
            return;
        }

        disposables.add(authService.login(usernameInput.getText(), passwordInput.getText())
                .observeOn(FX_SCHEDULER).subscribe(lr -> {
                    app.show(menuControllerProvider.get());
                }, error -> {
                    Dialog.error(error, "Login failed");
                }));
    }

    @FXML
    public void register() {
        app.show(registrationControllerProvider.get());
    }

    public void setDe() {
        setLanguage(Locale.GERMAN);
    }

    public void setEn() {
        setLanguage(Locale.ENGLISH);
    }

    private void setLanguage(Locale locale) {
        preferences.put("locale", locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.show(this);
    }


}
