package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

public class IngameController extends Controller {

    @FXML
    public HBox ingame;
    @FXML
    private Button pause;
    @Inject
    App app;
    /* TODO remove the comment surrounding the following lines, if PauseController is implemented
    @Inject
    Provider<PauseController> pauseControllerProvider;
    */

    @Inject
    public IngameController() {
    }

    @FXML
    public void pauseMenu(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            // TODO open PauseController
            // app.show(pauseControllerProvider.get());
        }
    }

    @Override
    public String getTitle() {
        return "Beastopia";
    }
}
