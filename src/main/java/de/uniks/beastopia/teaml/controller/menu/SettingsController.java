package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.utils.Prefs;
import de.uniks.beastopia.teaml.utils.SoundController;
import de.uniks.beastopia.teaml.utils.ThemeSettings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsController extends Controller {

    private final ArrayList<Controller> subControllers = new ArrayList<>();
    @FXML
    public RadioButton selectEnglishLanguage;
    @FXML
    public ToggleGroup language;
    @FXML
    public RadioButton selectGermanLanguage;
    @FXML
    public RadioButton darkRadioButton;
    @FXML
    public RadioButton lightRadioButton;
    @FXML
    public ToggleGroup theme;
    @FXML
    public Slider musicVolumeSlider;
    @FXML
    public Slider soundVolumeSlider;
    @FXML
    public Button backButton;
    @FXML
    public VBox vboxKeybindings;

    @Inject
    Prefs prefs;
    @Inject
    ThemeSettings themeSettings;
    @Inject
    Provider<ResourceBundle> resourcesProvider;
    @Inject
    Provider<KeybindElementController> keybindElementControllerProvider;
    @Inject
    Provider<SoundController> soundControllerProvider;
    SoundController soundController;
    double debounceDelay = 250; // Delay in milliseconds
    long lastValueChangeTime = 0;


    @Inject
    public SettingsController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        if (prefs.getLocale().contains("de")) {
            selectGermanLanguage.setSelected(true);
        } else {
            selectEnglishLanguage.setSelected(true);
        }

        darkRadioButton.setSelected(prefs.getTheme().equals("dark"));
        lightRadioButton.setSelected(!prefs.getTheme().equals("dark"));

        musicVolumeSlider.setValue(prefs.getMusicVolume());
        soundVolumeSlider.setValue(prefs.getSoundVolume());

        showKeyBindings();

        soundController = soundControllerProvider.get();

        return parent;
    }

    private void showKeyBindings() {
        vboxKeybindings.getChildren().clear();
        vboxKeybindings.getChildren().add(createController("ESC", "OpensPauseMenu").render());
        vboxKeybindings.getChildren().add(createController("W", "MoveUp").render());
        vboxKeybindings.getChildren().add(createController("A", "MoveLeft").render());
        vboxKeybindings.getChildren().add(createController("S", "MoveDown").render());
        vboxKeybindings.getChildren().add(createController("D", "MoveRight").render());
        vboxKeybindings.getChildren().add(createController("B", "OpensBeastList").render());
        vboxKeybindings.getChildren().add(createController("M", "OpenMap").render());
    }

    @FXML
    public void setEn() {
        setLanguage(Locale.ENGLISH);
    }

    @FXML
    public void setDe() {
        setLanguage(Locale.GERMAN);
    }

    private void setLanguage(Locale locale) {
        prefs.setLocale(locale.toLanguageTag());
        resources = resourcesProvider.get();
        app.update();
    }

    @FXML
    public void setDarkTheme() {
        prefs.setTheme("dark");
        themeSettings.updateSceneTheme.accept("dark");
    }

    @FXML
    public void setLightTheme() {
        prefs.setTheme("light");
        themeSettings.updateSceneTheme.accept("light");
    }

    @FXML
    public void changeMusicVolume() {
        prefs.setMusicVolume(musicVolumeSlider.getValue());

        if (soundController.getBgmPlayer() != null) {
            soundController.updateVolume();
        } else {
            soundController.play("bgm:city");
        }
    }

    @FXML
    public void changeSoundVolume() {
        soundVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            prefs.setSoundVolume(soundVolumeSlider.getValue());

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastValueChangeTime > debounceDelay) {
                soundController.play("sfx:bump");
                lastValueChangeTime = currentTime;
            }
        });
    }

    @FXML
    public void back() {
        app.showPrevious();

        if (soundController.getBgmPlayer() != null) {
            soundController.stopBGM();
        }
    }

    @Override
    public String getTitle() {
        return resources.getString("titleSettings");
    }

    private KeybindElementController createController(String key, String action) {
        KeybindElementController controller = keybindElementControllerProvider.get();
        controller.setKeyAndAction(key, action);
        subControllers.add(controller);
        return controller;
    }

    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
    }
}
