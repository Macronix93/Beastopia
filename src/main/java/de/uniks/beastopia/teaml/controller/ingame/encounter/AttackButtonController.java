package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.scene.control.Label;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class AttackButtonController extends Controller {

    private String attackName;
    private String attackType;
    private int accuracy;
    private int power;
    public Label attackLabel;
    public Label attackTypeLabel;
    public Label AccuracyLabel;
    public Label powerLabel;

    @Inject
    public AttackButtonController() {
    }

    public void setAttackName(String attackName) {
        this.attackName = attackName;
    }

    public void setAttackType(String attackType) {
        this.attackType = attackType;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
