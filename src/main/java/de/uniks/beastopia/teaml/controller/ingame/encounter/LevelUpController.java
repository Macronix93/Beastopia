package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class LevelUpController extends Controller {
    @FXML
    public Label headline;
    @FXML
    public ImageView image;
    @FXML
    public Label up_text;
    @FXML
    public Label attack;
    @FXML
    public Label type;
    @FXML
    public Label accuracy;
    @FXML
    public Label power;
    @FXML
    public Label up_text_bottom;
    @FXML
    public Label lifeValueLabel;
    @FXML
    public Label maxLifeLabel;
    @FXML
    public Label plusHP;
    @FXML
    public Label xpValueLabel;
    @FXML
    public Label maxXpLabel;
    @FXML
    public Button continueBtn;

    @Inject
    public LevelUpController() {

    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }


    @FXML
    public void continuePressed() {
    }
}
