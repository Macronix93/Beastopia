package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;

public class ItemController extends Controller {
    @FXML
    public ImageView img;
    @FXML
    public Label name;
    @FXML
    public Label count;

    @Inject
    public ItemController() {

    }

    @Override
    public Parent render() {
            Parent parent = super.render();
            return parent;
    }

    @FXML
    public void toggleDetails(MouseEvent mouseEvent) {
    }
}
