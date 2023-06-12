package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.TokenStorage;
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
    @Inject
    Provider<BeastController> beastControllerProvider;
    @Inject
    TokenStorage tokenStorage;

    private Runnable onCloseRequest;

    @Inject
    public BeastListController() {

    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        VBoxBeasts.getChildren().add(beastControllerProvider.get().render());
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
