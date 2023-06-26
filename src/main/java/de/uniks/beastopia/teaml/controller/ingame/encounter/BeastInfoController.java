package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class BeastInfoController extends Controller {

    @FXML
    Label name;
    @FXML
    Label type;
    @FXML
    Label level;
    @FXML
    Label lifeValueLabel;
    @FXML
    Label maxLifeLabel;
    @FXML
    Label xpValueLabel;
    @FXML
    Label maxXpLabel;

    @FXML
    HBox lifeBar;
    @FXML
    HBox lifeBarValue;
    @FXML
    HBox xpBar;
    @FXML
    HBox xpBarValue;

    @Inject
    PresetsService presetsService;
    private Monster monster;

    @Inject
    public BeastInfoController() {
    }

    public void setBeast(Monster monster) {
        this.monster = monster;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();


        return parent;
    }


    public void setLifeValueLabel(String value) {
        lifeValueLabel.setText(value);
    }

    public void setMaxLifeLabel(String value) {
        maxLifeLabel.setText(value);
    }

    public void setXpValueLabel(String value) {
        xpValueLabel.setText(value);
    }

    public void setMaxXpLabel(String value) {
        maxXpLabel.setText(value);
    }

    public void setName(String value) {
        name.setText(value);
    }

    public void setType(String value) {
        type.setText(value);
    }

    public void setLevel(String value) {
        level.setText(value);
    }

    public void setLifeBarValue(double value) {
        lifeBarValue.setPrefWidth(value);
    }

    public void setXpBarValue(double value) {
        xpBarValue.setPrefWidth(value);
    }


}
