package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InventoryController extends Controller {

    private final List<Item> items = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public VBox VBoxInvList;
    @FXML
    public VBox VBoxItems;
    private final List<Item> items = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();

    @Inject
    public InventoryController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
    }

    @FXML
    public void close(ActionEvent actionEvent) {
    }
}
