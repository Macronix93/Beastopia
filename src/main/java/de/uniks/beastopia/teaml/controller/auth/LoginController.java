package de.uniks.beastopia.teaml.controller.auth;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class LoginController extends Controller {
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();
    @FXML
    public ComboBox<String> usernameInput;
    @FXML
    public PasswordField passwordInput;
    @FXML
    public Button loginButton;
    @FXML
    public CheckBox rememberMe;
    @FXML
    public RadioButton selectEnglishLanguage;
    @FXML
    public RadioButton selectGermanLanguage;
    @FXML
    public ImageView banner;
    @FXML
    public HBox backroundHbox;
    @FXML
    public GridPane innerGridPane;
    @FXML
    public Text singupText;
    @FXML
    public ImageView companyLogo;
    @Inject
    Provider<RegistrationController> registrationControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    Prefs prefs;
    @Inject
    ImageService imageService;
    @Inject
    Provider<ResourceBundle> resourcesProvider;
    private List<String> userHistory = new ArrayList<>();
    private BooleanBinding isInValid;
    private LoadingPage loadingPage;

    @Inject
    public LoginController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleLogin");
    }

    @Override
    public void init() {
        if (prefs.isRememberMe()) {
            disposables.add(authService.refresh().observeOn(FX_SCHEDULER).subscribe(
                    lr -> {
                        app.show(menuControllerProvider.get());
                        if (loadingPage != null) {
                            loadingPage.setDone();
                        }
                    },
                    error -> {
                        if (loadingPage != null) {
                            loadingPage.setDone();
                        }
                        Dialog.error(error, "Remember me failed!");
                    }));
        }
        userHistory = prefs.getUserHistory();
        Collections.reverse(userHistory);
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        if (!prefs.isRememberMe()) {
            onUI(() -> loadingPage.setDone());
        }

        banner.setImage(imageService.getBanner());
        companyLogo.setImage(imageService.getCompanyLogo());
        usernameInput.valueProperty().bindBidirectional(username);
        passwordInput.textProperty().bindBidirectional(password);
        usernameInput.getItems().addAll(userHistory);
        usernameInput.setOnAction(event -> username.set(usernameInput.getSelectionModel().getSelectedItem()));
        usernameInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                username.set(newValue);
            }
        });
        if (prefs.getLocale().contains("de")) {
            selectGermanLanguage.setSelected(true);
        } else {
            selectEnglishLanguage.setSelected(true);
        }


        isInValid = username.isEmpty().or(password.length().lessThan(8));
        loginButton.disableProperty().bind(isInValid);

        return loadingPage.parent();
    }

    @FXML
    public void login() {
        if (isInValid.get()) {
            return;
        }

        disposables.add(authService.login(usernameInput.getSelectionModel().getSelectedItem(), passwordInput.getText(), rememberMe.isSelected())
                .observeOn(FX_SCHEDULER).subscribe(
                        lr -> {
                            if (userHistory.size() == 5 && !userHistory.contains(lr.name())) {
                                userHistory.remove(4);
                            }
                            Collections.reverse(userHistory);
                            if (!userHistory.contains(lr.name())) {
                                userHistory.add(lr.name());
                            }
                            prefs.setUserHistory(userHistory);
                            app.show(menuControllerProvider.get());
                        },
                        error -> Dialog.error(error, "Login failed!")));
    }

    @FXML
    public void register() {
        app.show(registrationControllerProvider.get());
    }

    public void setDe() {
        setLanguage(Locale.GERMAN);
    }

    public void setEn() {
        setLanguage(Locale.ENGLISH);
    }

    private void setLanguage(Locale locale) {
        prefs.setLocale(locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
        if (loadingPage != null) {
            loadingPage.setDone();
        }
    }
}
