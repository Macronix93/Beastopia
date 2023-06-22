package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class EnemyBeastInfoController extends Controller {

    @FXML
    Label enemyName;
    @FXML
    Label enemyLevel;
    @Inject
    PresetsService presetsService;
    private Monster monster;

    @Inject
    public EnemyBeastInfoController() {
    }


}
