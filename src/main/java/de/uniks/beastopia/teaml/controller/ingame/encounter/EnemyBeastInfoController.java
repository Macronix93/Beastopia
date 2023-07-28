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

    public EnemyBeastInfoController setMonster(Monster monster) {
        this.monster = monster;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(presetsService.getMonsterType(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> enemyName.setText(monsterType.name())));
        enemyLevel.setText(String.valueOf(monster.level()));

        //TODO: calculate lifebar value, call setLifeBarValue()

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> setLifeBarValue(monster.currentAttributes().health() / (double) monster.attributes().health()));
            }
        }, 500);

        return parent;
    }

    public void setLifeBarValue(double value) {
        lifeBarValue.setMinWidth(lifeBar.getWidth() * value);
        lifeBarValue.setMaxWidth(lifeBar.getWidth() * value);
        lifeBarValue.setPrefWidth(lifeBar.getWidth() * value);
    }

    public Monster getMonster() {
        return this.monster;
    }

    public void setLevel(int level) {
        this.enemyLevel.setText(String.valueOf(level));
    }

    public void setName(String name) {
        this.enemyName.setText(name);
    }

    public String getName() {
        return enemyName.getText();
    }
}