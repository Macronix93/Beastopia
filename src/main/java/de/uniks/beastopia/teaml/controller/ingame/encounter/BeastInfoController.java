package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.AssetProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("unused")
public class BeastInfoController extends Controller {


    @FXML
    HBox statusPocket;
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
    @Inject
    AssetProvider assets;
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
        hpLabel.setText((int) monster.currentAttributes().health() + " / " + (int) monster.attributes().health() + " (HP)");
        xpLabel.setText(monster.experience() + " / " + calcMaxXp() + " (Exp)");
        setStatus(monster.status(), false);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    setLifeBarValue(monster.currentAttributes().health() / monster.attributes().health(), false);
                    setXpBarValue(monster.experience() / (double) calcMaxXp(), false);
                    setStatus(monster.status(), false);
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

    public void setLevel(int level) {
        this.level.setText(String.valueOf(level));
    }

    public void setName(String name) {
        this.name.setText(name);
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
        killTimer(killTimer);
        xpBarValue.setMinWidth(xpBar.getWidth() * value);
        xpBarValue.setMaxWidth(xpBar.getWidth() * value);
        xpBarValue.setPrefWidth(xpBar.getWidth() * value);
    }

    public void setStatus(List<String> status, boolean killTimer) {
        killTimer(killTimer);
        statusPocket.getChildren().clear();
        if (!monster.status().isEmpty()) {
            status.forEach(element -> statusPocket.getChildren().add(assets.getIcon("status", element, 25, 25)));
        }
    }

    private void killTimer(boolean kill) {
        if (kill) {
            timer.cancel();
        }
    }

    public Monster getMonster() {
        return monster;
    }

    public String getName() {
        return name.getText();
    }
}
