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

    //TODO: add hp and xp
    Label hp;
    Label xp;

    @FXML
    Label name;
    @FXML
    Label type;
    @FXML
    Label level;
    @FXML
    HBox lifeBar;
    @FXML
    HBox lifeBarValue;
    @FXML
    HBox xpBar;
    @FXML
    HBox xpBarValue;

    @FXML
    Label lifeValueLabel;
    @FXML
    Label maxLifeLabel;
    @FXML
    Label xpValueLabel;
    @FXML
    Label maxXpLabel;
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

    public void setLifeValueLabel(Label lifeValueLabel) {
        this.lifeValueLabel = lifeValueLabel;
    }

    public void setMaxLifeLabel(Label maxLifeLabel) {
        this.maxLifeLabel = maxLifeLabel;
    }

    public void setXpValueLabel(Label xpValueLabel) {
        this.xpValueLabel = xpValueLabel;
    }

    public void setMaxXpLabel(Label maxXpLabel) {
        this.maxXpLabel = maxXpLabel;
    }


}
