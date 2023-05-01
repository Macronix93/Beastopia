package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.service.RegistrationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import retrofit2.HttpException;

import javax.inject.Inject;

public class RegistrationController extends Controller {

    private static final String LUMNIX_LOGO_URL = "https://db3pap006files.storage.live.com/y4mxQt5LQarTNXi_kqABPhZym0Mz3F9OlGfaD_oTWOO9bOQ3O5ONq7RuM7MAs0jYC0KvrQzmUYviHw0_u5iWumlA_h1uJ8nkOdVyO1xjk5IP6DGAWWeLRBN4rTch1Pmtr0220reSsrz7T8FRApdi3u7U_0hgat5RaXj4_fO7xp-lAXwCSQoNIPhazCcAoEPtDKk?encodeFailures=1&width=500&height=500";

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField passwordRepeatInput;

    @Inject
    RegistrationService registrationService;

    @Inject
    App app;

    @Inject
    public RegistrationController() {
    }

    @Override
    public String getTitle() {
        return "Beastopia - Registration";
    }

    @FXML
    private void uploadAvatar() {

    }

    @FXML
    private void signUp() {
        if (!passwordInput.getText().equals(passwordRepeatInput.getText())) {
            // TODO: Show error message in dialog
            return;
        }

        registrationService.createUser(usernameInput.getText(), LUMNIX_LOGO_URL, passwordInput.getText())
                .subscribe(user -> {
                    // TODO: Show success message in dialog and show LoginController
                }, error -> {
                    Platform.runLater(() -> {
                        if (error instanceof HttpException httpError) {
                            // TODO: Show error message in dialog
                        }
                    });
                });

    }

    @FXML
    private void switchToSignIn() {
        // TODO: Implement when login is implemented
//        main.app().show(main.loginController());
    }
}
