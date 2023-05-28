package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController extends Controller {
    @FXML
    public TextField usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Button loginButton;
    @FXML
    public CheckBox rememberMe;
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
    @SuppressWarnings("unused")
    @Inject
    TokenStorage tokenStorage;
    @SuppressWarnings("unused")
    @Inject
    Prefs prefs;
    @Inject
    Provider<ResourceBundle> resourcesProvider;

    private BooleanBinding isInValid;
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();

    @Inject
    public LoginController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleLogin");
    }

    @Override
    public void init() {
        if (prefs.isRememberMe()) {
            disposables.add(authService.refresh().observeOn(FX_SCHEDULER).subscribe(
                    lr -> app.show(menuControllerProvider.get()),
                    error -> Dialog.error(error, "Remember me failed!")));
        }
    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

        if (prefs.getLocale().contains("de")) {
            selectGermanLanguage.setSelected(true);
        } else {
            selectEnglishLanguage.setSelected(true);
        }
        usernameInput.textProperty().bindBidirectional(username);
        passwordInput.textProperty().bindBidirectional(password);

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

        disposables.add(authService.login(usernameInput.getText(), passwordInput.getText(), rememberMe.isSelected())
                .observeOn(FX_SCHEDULER).subscribe(
                        lr -> app.show(menuControllerProvider.get()),
                        error -> Dialog.error(error, "Login failed")));
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
        prefs.setLocale(locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
    }

}
