package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class BeastController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label lastname;
    @FXML
    public Label hp;
    @FXML
    public Label level;

    @Inject
    public BeastController() {

    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    public void openDetails() {

    }
}
