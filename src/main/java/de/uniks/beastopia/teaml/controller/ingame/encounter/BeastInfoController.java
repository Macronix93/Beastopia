package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

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


}
