package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ItemDetailController extends Controller {

    @FXML
    public VBox VBoxItemDetail;
    @FXML
    public Label cost;
    @FXML
    public Label name;
    @FXML
    public ImageView itemImage;
    @FXML
    public Button shopBtn;
    @FXML
    public Label desc;

    @Inject
    public ItemDetailController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void shopFunction(ActionEvent actionEvent) {

    }
}
