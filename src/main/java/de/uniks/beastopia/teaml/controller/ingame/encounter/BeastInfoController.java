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
    Label hpLabel;
    @FXML
    Label xpLabel;

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

    public BeastInfoController setMonster(Monster monster) {
        this.monster = monster;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(presetsService.getMonsterType(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> {
                    name.setText(monsterType.name());
                    type.setText(monsterType.type().get(0));
                }));
        level.setText(String.valueOf(monster.level()));
        hpLabel.setText(monster.currentAttributes().health() + " / " + monster.attributes().health() + " (HP)");
        xpLabel.setText(monster.experience() + " / " + calcMaxXp() + " (Exp)");

        return parent;
    }

    private long calcMaxXp() {
        return Math.round(Math.pow(monster.level(), 3) - Math.pow((monster.level() - 1), 3));
    }

    public void setType(String value) {
        type.setText(value);
    }

    public void setLifeBarValue(double value) {
        lifeBarValue.setPrefWidth(value);
    }

    public void setXpBarValue(double value) {
        xpBarValue.setPrefWidth(value);
    }


}
