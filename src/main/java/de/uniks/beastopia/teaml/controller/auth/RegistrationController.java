package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.RegistrationService;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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
    @FXML
    public RadioButton selectEnglishLanguage;
    @FXML
    public RadioButton selectGermanLanguage;

    @Inject
    Provider<ResourceBundle> resourcesProvider;
    @Inject
    RegistrationService registrationService;
    @Inject
    Preferences preferences;
    @Inject
    Provider<LoginController> loginControllerProvider;

    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty passwordRepeat = new SimpleStringProperty();
    private boolean isEnglish = false;

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

        String userLocale = Locale.getDefault().toLanguageTag();
        if (!userLocale.equals("en-EN") && !userLocale.equals("de-DE")) {
            userLocale = "en-EN";
        }

        if (preferences.get("locale", userLocale).contains("de")) {
            selectGermanLanguage.setSelected(true);
        } else {
            selectEnglishLanguage.setSelected(true);
        }
        usernameInput.textProperty().bindBidirectional(username);
        passwordInput.textProperty().bindBidirectional(password);
        passwordRepeatInput.textProperty().bindBidirectional(passwordRepeat);

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
                    Dialog.info(isEnglish ? "Registration successful!" : "Registrierung erfolgreich!",
                            isEnglish ? "You can now sign in with your new account." : "Sie k\u00f6nnen sich jetzt mit Ihrem neuen Konto anmelden.");
                    app.show(loginControllerProvider.get());
                }, error -> Dialog.error(error, isEnglish ? "Registration failed." : "Registrierung fehlgeschlagen.")));
    }

    @FXML
    private void switchToSignIn() {
        app.show(loginControllerProvider.get());
    }

    public void setDe() {
        setLanguage(Locale.GERMAN);
        isEnglish = false;
    }

    public void setEn() {
        setLanguage(Locale.ENGLISH);
        isEnglish = true;
    }

    private void setLanguage(Locale locale) {
        preferences.put("locale", locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
    }
}
