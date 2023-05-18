package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;
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
    Provider<ResourceBundle> resourcesProvider;

    private BooleanBinding isInValid;

    @Inject
    public LoginController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleLogin");
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

        disposables.add(authService.login(usernameInput.getText(), passwordInput.getText(), rememberMe.isSelected())
                .observeOn(FX_SCHEDULER).subscribe(
                        lr -> app.show(menuControllerProvider.get()),
                        error -> Dialog.error(error, "Login failed")));
    }

    @FXML
    public void register() {
        app.show(registrationControllerProvider.get());
    }
}
