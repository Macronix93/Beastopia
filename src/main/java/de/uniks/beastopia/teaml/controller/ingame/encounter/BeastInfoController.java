package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timer;

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
                    type.setText("(" + monsterType.type().get(0) + ")");
                }));
        level.setText(String.valueOf(monster.level()));
        hpLabel.setText(monster.currentAttributes().health() + " / " + monster.attributes().health() + " (HP)");
        xpLabel.setText(monster.experience() + " / " + calcMaxXp() + " (Exp)");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    setLifeBarValue(monster.currentAttributes().health() / (double) monster.attributes().health(), false);
                    setXpBarValue(monster.experience() / (double) calcMaxXp(), false);
                });
            }
        }, 500);

        return parent;
    }

    private long calcMaxXp() {
        return Math.round(Math.pow(monster.level(), 3) - Math.pow((monster.level() - 1), 3));
    }

    public void setType(String value) {
        type.setText(value);
    }

    public void setLifeBarValue(double value, boolean killTimer) {
        if (killTimer) {
            timer.cancel();
            setXpBarValue(monster.experience() / (double) calcMaxXp(), false);
        }
        lifeBarValue.setMinWidth(lifeBar.getWidth() * value);
        lifeBarValue.setMaxWidth(lifeBar.getWidth() * value);
        lifeBarValue.setPrefWidth(lifeBar.getWidth() * value);
    }

    public void setXpBarValue(double value, boolean killTimer) {
        if (killTimer) {
            timer.cancel();
        }
        xpBarValue.setMinWidth(xpBar.getWidth() * value);
        xpBarValue.setMaxWidth(xpBar.getWidth() * value);
        xpBarValue.setPrefWidth(xpBar.getWidth() * value);
    }

    public Monster getMonster() {
        return monster;
    }

    public String getName() {
        return name.getText();
    }
}
