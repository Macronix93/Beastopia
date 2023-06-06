package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class BeastListController extends Controller {

    @FXML
    public VBox VBoxBeasts;
    @Inject
    Provider<IngameController> ingameControllerProvider;

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
    }
}
