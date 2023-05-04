package de.uniks.beastopia.teaml.controller;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.rest.ErrorResponse;
import de.uniks.beastopia.teaml.service.RegistrationService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import retrofit2.HttpException;

import javax.inject.Inject;
import javax.inject.Provider;

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
    Provider<LoginController> loginControllerProvider;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.setContentText("The passwords you entered do not match. Please try again.");
            alert.showAndWait();
            return;
        }

        disposables.add(registrationService.createUser(usernameInput.getText(), LUMNIX_LOGO_URL, passwordInput.getText())
                .subscribe(user -> {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Registration successful");
                        alert.setContentText("The registration was successful. You can now sign in.");
                        alert.showAndWait();
                        // TODO: show login when implemented
                    });
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
                            alert.setHeaderText("Registration failed");
                            alert.setContentText(message);
                            alert.showAndWait();
                        }
                    });
                }));
    }

    @FXML
    private void switchToSignIn() {
        app.show(loginControllerProvider.get());
    }
}
