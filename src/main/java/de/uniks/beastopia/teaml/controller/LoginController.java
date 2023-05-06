package de.uniks.beastopia.teaml.controller;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class LoginController extends Controller {
    @FXML
    public TextField usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Button loginButton;

    @Inject
    App app;
    @Inject
    Provider<RegistrationController> registrationControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;

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

        disposables.add(authService.login(usernameInput.getText(), passwordInput.getText()).subscribe(lr -> {
            Platform.runLater(() -> {
                app.show(menuControllerProvider.get());
            });
        }, error -> {
            Dialog.error(error, "Login failed");
        }));
    }

    @FXML
    public void register() {
        app.show(registrationControllerProvider.get());
    }
}
