package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.RegistrationService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Provider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrationController extends Controller {

    @FXML
    public ImageView avatarPreview;
    @FXML
    public ImageView banner;
    @FXML
    public HBox backroundHbox;
    @FXML
    public GridPane innerGridPane;
    @FXML
    public Button login;
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
    Prefs prefs;
    @Inject
    Provider<LoginController> loginControllerProvider;
    @Inject
    DataCache cache;

    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    private final SimpleStringProperty passwordRepeat = new SimpleStringProperty();
    private boolean isEnglish = false;
    private BufferedImage bufferedImage;

    @Inject
    public RegistrationController() {
    }

    @Override
    public void init() {
        super.init();
        try {
            bufferedImage = ImageIO.read(new File(Objects.requireNonNull(Main.class.getResource("assets/user.png")).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTitle() {
        return resources.getString("titleRegistration");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        banner.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/beastopia_banner.png"))));
        avatarPreview.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/user.png"))));

        if (prefs.getLocale().contains("de")) {
            selectGermanLanguage.setSelected(true);
            isEnglish = false;
        } else {
            selectEnglishLanguage.setSelected(true);
            isEnglish = true;
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
        File file = cache.provideImageFile(app);
        if (file == null) return;
        this.bufferedImage = cache.provideBufferedImage(file);
        avatarPreview.setImage(new Image(file.toURI().toString(), 128, 128, true, true, true));
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    @FXML
    private void signUp() {
        disposables.add(registrationService.createUser(usernameInput.getText(), cache.getAvatarDataUrl(bufferedImage), passwordInput.getText())
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
        prefs.setLocale(locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
    }

    @Override
    public void destroy() {
        super.destroy();
        avatarPreview = null;
    }
}
