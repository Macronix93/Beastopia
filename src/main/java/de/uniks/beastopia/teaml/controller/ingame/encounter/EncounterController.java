package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    Label leftAttackTypeLabel;
    @FXML
    Label rightAttackTypeLabel;
    @FXML
    Label leftAccLabel;
    @FXML
    Label rightAccLabel;
    @FXML
    Label leftPowerLabel;
    @FXML
    Label rightPowerLabel;
    @FXML
    Label leftAttackLabel;
    @FXML
    Label rightAttackLabel;
    @FXML
    VBox enemyMonstersBox;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;

    //monster on the substitute's bench
    private List<Monster> ownMonsters = new ArrayList<>();
    private List<Monster> allyMonsters = new ArrayList<>();
    private List<Monster> enemyMonsters = new ArrayList<>();
    private List<Monster> enemyAllyMonsters = new ArrayList<>();

    //monsters in the fight
    private Monster ownMonster;
    private Monster allyMonster;
    private Monster enemyMonster;
    private Monster enemyAllyMonster;

    private Trainer allyTrainer;
    private Trainer enemyTrainer;
    private Trainer enemyAllyTrainer;
    private boolean oneVsOneFight = true;
    private boolean oneVsOneFightMonsterOnly = false;
    private boolean oneVsTwoFight = false;
    private boolean twoVsTwoFight = false;

    @Inject
    public EncounterController() {
    }

    @Override
    public void init() {
        setFightMode();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        getOwnMonsters();
        if (enemyTrainer != null) {
            getEnemyTrainerMonsters();
        } else {
            getEnemyMonster();
        }
        return parent;
    }

    //TODO: set no of possible attacks dynamically
    /*
    create Vboxes for each attack
    add them to parent of leftAttackBox and rightAttackBox
    leftAttackBox.setVisible(true);
    rightAttackBox.setVisible(true);
    .
    .
    .
    */

    private void setFightMode() {
        if (enemyAllyTrainer != null && allyTrainer != null) {
            oneVsOneFight = false;
            twoVsTwoFight = true;
        } else if (allyTrainer == null && enemyAllyTrainer != null) {
            oneVsOneFight = false;
            oneVsTwoFight = true;
        } else if (allyTrainer == null && enemyTrainer == null) {
            oneVsOneFight = false;
            oneVsOneFightMonsterOnly = true;
        }

    }

    private void getEnemyMonster() {
        //TODO: get enemy monster
    }

    private void getEnemyTrainerMonsters() {
        //TODO: get ID of enemy trainer and enemy trainer monsters
    }

    public void getOwnMonsters() {
        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    this.ownMonsters.addAll(monsters);
                }));
    }

    //clicked leave encounter button
    public void leaveEncounter() {
        //TODO: switch screen to map
        System.out.println("leave encounter");
    }

    //clicked change monster button
    public void changeMonster() {
        //TODO: switch screen to monster selection
        System.out.println("change monster");
    }

    //clicked left attack VBox
    public void leftAttack() {
        System.out.println("left attack");
    }

    //clicked right attack VBox
    public void rightAttack() {
        System.out.println("right attack");
    }

    public void setOwnMonsters(List<Monster> ownMonsters) {
        this.ownMonsters = ownMonsters;
    }

    public void setAllyMonsters(List<Monster> allyMonsters) {
        this.allyMonsters = allyMonsters;
    }

    public void setEnemyMonsters(List<Monster> enemyMonsters) {
        this.enemyMonsters = enemyMonsters;
    }

    public void setEnemyAllyMonsters(List<Monster> enemyAllyMonsters) {
        this.enemyAllyMonsters = enemyAllyMonsters;
    }

    public void setAllyTrainer(Trainer allyTrainer) {
        this.allyTrainer = allyTrainer;
    }

    public void setEnemyTrainer(Trainer enemyTrainer) {
        this.enemyTrainer = enemyTrainer;
    }

    public void setEnemyAllyTrainer(Trainer enemyAllyTrainer) {
        this.enemyAllyTrainer = enemyAllyTrainer;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

}
