package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

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

    @Inject
    public EncounterController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

}
