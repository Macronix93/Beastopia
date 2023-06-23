package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;

public class EncounterController extends Controller {

    @FXML
    private VBox leftAttackBox;
    @FXML
    private VBox rightAttackBox;
    @FXML
    Button leaveEncounter;
    @FXML
    Button changeMonster;
    @FXML
    VBox actionInfoBox;
    @FXML
    VBox beastInfoBox;
    @FXML
    Label leftAttackTypeLabel;
    @FXML
    Label rightAttackTypeLabel;
    @FXML
    Label leftAccLabel;
    @FXML
    Label rightAccLabel;
    @FXML
    Label leftPowerLabel;
    @FXML
    Label rightPowerLabel;
    @FXML
    Label leftAttackLabel;
    @FXML
    Label rightAttackLabel;

    private ArrayList<Monster> enemyTeam;
    private ArrayList<Monster> ownTeam;

    @Inject
    public EncounterController() {
    }

    @Override
    public void init() {
        //TODO: set enemyTeam and ownTeam
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    //clicked leave encounter button
    public void leaveEncounter() {
        //TODO: switch screen to map
        System.out.println("leave encounter");
    }

    //clicked change monster button
    public void changeMonster() {
        //TODO: switch screen to monster selection
        System.out.println("change monster");
    }

    //clicked left attack VBox
    public void leftAttack() {
        System.out.println("left attack");
    }

    //clicked right attack VBox
    public void rightAttack() {
        System.out.println("right attack");
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

}
