package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteUserController extends Controller {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    Button deleteUserButton;
    @FXML
    Button cancelButton;
    @Inject
    Prefs prefs;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Provider<LoginController> loginControllerProvider;

    @Inject
    public DeleteUserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        usernameField.setText(tokenStorage.getCurrentUser().name());
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleDeleteUser");
    }

    public void deleteUser() {
        if (!passwordField.getText().isEmpty()) {
            disposables.add(authService.login(tokenStorage.getCurrentUser().name(), passwordField.getText(), false)
                    .observeOn(FX_SCHEDULER).subscribe(
                            lr -> deleteConfirmed(),
                            error -> Dialog.error(error, resources.getString("deleteFailed"))));
        }
    }

    private void deleteConfirmed() {
        disposables.add(authService.deleteUser()
                .observeOn(FX_SCHEDULER).subscribe(
                        lr -> {
                            prefs.clearRememberMe();
                            app.show(loginControllerProvider.get());
                        },
                        error -> Dialog.error(error, resources.getString("deleteFailed"))));
    }

    public void cancel() {
        app.showPrevious();
    }
}

