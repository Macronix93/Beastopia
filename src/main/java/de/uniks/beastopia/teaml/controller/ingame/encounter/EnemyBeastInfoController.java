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
public class EnemyBeastInfoController extends Controller {

    @FXML
    Label enemyName;
    @FXML
    Label enemyLevel;
    @FXML
    HBox lifeBar;
    @FXML
    HBox lifeBarValue;
    @Inject
    PresetsService presetsService;
    private Monster monster;

    @Inject
    public EnemyBeastInfoController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();


        return parent;
    }

    public void setEnemyName(String value) {
        enemyName.setText(value);
    }

    public void setEnemyLevel(String value) {
        enemyLevel.setText(value);
    }

    public void setLifeBarValue(double value) {
        lifeBarValue.setPrefWidth(value);
    }


}
