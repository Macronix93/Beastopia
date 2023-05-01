package de.uniks.beastopia.teaml.controller;
import de.uniks.beastopia.teaml.App;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

public class RegistrationController extends Controller {

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField passwordRepeatInput;
    private final App app;

    public RegistrationController(App app) {
        this.app = app;
    }


    @FXML
    private void uploadAvatar() {

    }

    @FXML
    private void signUp() {
    }

    @FXML
    private void switchToSignIn() {

    }

}
