package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {
    @FXML
    public HBox ingame;
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
        return resources.getString("titleIngame");
    }
}
