package de.uniks.beastopia.teaml.controller;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.rest.ErrorResponse;
import de.uniks.beastopia.teaml.service.LoginService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import retrofit2.HttpException;

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
    LoginService loginService;
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

        disposables.add(loginService.login(usernameInput.getText(), passwordInput.getText()).subscribe(lr -> {
            app.show(new MenuController());
        }, error -> {
            Platform.runLater(() -> {
                if (error instanceof HttpException httpError) {
                    String message;
                    try {
                        String json = httpError.response().errorBody().string();
                        ErrorResponse response = new Gson().fromJson(json, ErrorResponse.class);
                        message = response.message();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Login failed");
                    alert.setContentText(message);
                    alert.showAndWait();
                }
            });
        }));
    }

    @FXML
    public void register() {
        app.show(registrationControllerProvider.get());
    }
}
