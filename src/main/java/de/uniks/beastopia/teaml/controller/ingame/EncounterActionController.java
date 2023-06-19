package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.scene.control.Label;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class EncounterActionController extends Controller {

    private String attackerBeastName;
    private String defenderBeastName;
    public Label actionLabel;

    @Inject
    public EncounterActionController() {
    }

    public void setAttackerBeastName(String attackerBeastName) {
        this.attackerBeastName = attackerBeastName;
    }

    public void setDefenderBeastName(String defenderBeastName) {
        this.defenderBeastName = defenderBeastName;
    }
}
