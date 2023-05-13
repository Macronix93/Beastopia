package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.RegistrationService;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class RegistrationController extends Controller {

    private static final String LUMNIX_LOGO_URL = "https://db3pap006files.storage.live.com/y4mxQt5LQarTNXi_kqABPhZym0Mz3F9OlGfaD_oTWOO9bOQ3O5ONq7RuM7MAs0jYC0KvrQzmUYviHw0_u5iWumlA_h1uJ8nkOdVyO1xjk5IP6DGAWWeLRBN4rTch1Pmtr0220reSsrz7T8FRApdi3u7U_0hgat5RaXj4_fO7xp-lAXwCSQoNIPhazCcAoEPtDKk?encodeFailures=1&width=500&height=500";

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private PasswordField passwordRepeatInput;

    @FXML
    private Button signUpButton;

    @Inject
    RegistrationService registrationService;

    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    public RegistrationController() {
    }

    @Override
    public String getTitle() {
        return "Beastopia - Registration";
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        BooleanBinding isInValid = usernameInput.textProperty().isEmpty()
                .or(passwordInput.textProperty().length().lessThan(8))
                .or(passwordRepeatInput.textProperty().length().lessThan(8))
                .or(passwordInput.textProperty().isNotEqualTo(passwordRepeatInput.textProperty()));
        signUpButton.disableProperty().bind(isInValid);

        return parent;
    }

    @FXML
    private void uploadAvatar() {

    }

    @FXML
    private void signUp() {
        disposables.add(registrationService.createUser(usernameInput.getText(), LUMNIX_LOGO_URL, passwordInput.getText())
                .observeOn(FX_SCHEDULER).subscribe(user -> {
                    Dialog.info("Registration successful", "You can now sign in with your new account.");
                    app.show(loginControllerProvider.get());
                }, error -> Dialog.error(error, "Registration failed")));
    }

    @FXML
    private void switchToSignIn() {
        app.show(loginControllerProvider.get());
    }
}
