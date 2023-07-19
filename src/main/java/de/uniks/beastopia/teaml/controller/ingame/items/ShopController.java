package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ShopController extends Controller {

    @FXML
    public VBox VBoxShopList;
    @FXML
    public VBox VBoxItems;

    @Inject
    public ShopController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void close(ActionEvent actionEvent) {
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
    }
}
