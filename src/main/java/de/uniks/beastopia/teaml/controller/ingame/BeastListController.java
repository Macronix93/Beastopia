package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class BeastListController extends Controller {

    @FXML
    public VBox VBoxBeasts;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    private Runnable onCloseRequest;

    @Inject
    public BeastListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    @FXML
    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.B)) {
            onCloseRequest.run();
        }
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }
}
