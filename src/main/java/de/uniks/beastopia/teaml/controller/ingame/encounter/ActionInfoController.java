package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.scene.control.Label;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class ActionInfoController extends Controller {

    private String attackerBeastName;
    private String defenderBeastName;
    public Label actionLabel;

    @Inject
    public ActionInfoController() {
    }

    public void setAttackerBeastName(String attackerBeastName) {
        this.attackerBeastName = attackerBeastName;
    }

    public void setDefenderBeastName(String defenderBeastName) {
        this.defenderBeastName = defenderBeastName;
    }
}
