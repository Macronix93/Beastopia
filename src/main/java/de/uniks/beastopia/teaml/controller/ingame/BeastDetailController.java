package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
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
    private Monster monster;

    @Inject
    public BeastDetailController() {

    }

    public void setBeast(Monster monster) {
        this.monster = monster;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
//        name.setText(monster.name());
        return parent;
    }
}
