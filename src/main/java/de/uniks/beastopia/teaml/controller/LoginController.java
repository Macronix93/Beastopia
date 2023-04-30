package de.uniks.beastopia.teaml.controller;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javax.inject.Inject;

public class LoginController extends Controller {
    @FXML
    public TextField usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Button loginButton;
    @FXML
    public Button signUpButton;
    @FXML
    public CheckBox englishCheckBox;
    @FXML
    public CheckBox germanCheckBox;
    private BooleanBinding isInValid;

    @Inject
    public LoginController() {

    }

    @Override
    public Parent render() {
        final Parent parent = super.render();

         isInValid = usernameInput.textProperty().isEmpty()
                .or(passwordInput.textProperty().length().lessThan(8));
        loginButton.disableProperty().bind(isInValid);

        return parent;
    }

    public void login() {
        if (isInValid.get()) {
            return;
        }
    }

    public void register() {

    }
}
