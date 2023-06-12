package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.inject.Inject;

public class BeastDetailController extends Controller {

    @FXML
    public Label name;
    @FXML
    public Label hp;
    @FXML
    public Label speed;
    @FXML
    public Label level;
    @FXML
    public Label type;
    @FXML
    public Label attack;
    @FXML
    public Label experience;
    @FXML
    public Label defense;
    @FXML
    public TextArea description;

    @Inject
    public BeastDetailController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }
}
