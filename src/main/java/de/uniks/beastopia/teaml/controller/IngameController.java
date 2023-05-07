package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
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
    private Button pauseButton;
    @Inject
    App app;
    @Inject
    Provider<PauseController> pauseControllerProvider;

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
        return "Beastopia";
    }
}
