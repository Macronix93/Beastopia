package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.Trainer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {
    @FXML
    public HBox ingame;
    @FXML
    public Button pauseButton;
    @Inject
    App app;
    @Inject
    Provider<PauseController> pauseControllerProvider;

    Trainer trainer;

    @Inject
    public IngameController() {
    }

    @FXML
    public void pauseMenu(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            app.show(pauseControllerProvider.get());
        }
    }

    @Override
    public String getTitle() {
        return resources.getString("titleIngame");
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}
