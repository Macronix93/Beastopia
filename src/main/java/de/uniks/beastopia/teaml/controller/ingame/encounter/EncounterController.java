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
    VBox enemyMonstersBox;
    @FXML
    Label attackNameLabel1;
    @FXML
    Label attackTypeLabel1;
    @FXML
    Label accLabel1;
    @FXML
    Label powerLabel1;
    @FXML
    Label attackNameLabel2;
    @FXML
    Label attackTypeLabel2;
    @FXML
    Label accLabel2;
    @FXML
    Label powerLabel2;
    @FXML
    Label attackNameLabel3;
    @FXML
    Label attackTypeLabel3;
    @FXML
    Label accLabel3;
    @FXML
    Label powerLabel3;
    @FXML
    Label attackNameLabel4;
    @FXML
    Label attackTypeLabel4;
    @FXML
    Label accLabel4;
    @FXML
    Label powerLabel4;


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
    private List<Controller> subControllers = new ArrayList<>();

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

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
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

    public void setAttackNameLabel1(Label attackNameLabel1) {
        this.attackNameLabel1 = attackNameLabel1;
    }

    public void setAttackTypeLabel1(Label attackTypeLabel1) {
        this.attackTypeLabel1 = attackTypeLabel1;
    }

    public void setAccLabel1(Label accLabel1) {
        this.accLabel1 = accLabel1;
    }

    public void setPowerLabel1(Label powerLabel1) {
        this.powerLabel1 = powerLabel1;
    }

    public void setAttackNameLabel2(Label attackNameLabel2) {
        this.attackNameLabel2 = attackNameLabel2;
    }

    public void setAttackTypeLabel2(Label attackTypeLabel2) {
        this.attackTypeLabel2 = attackTypeLabel2;
    }

    public void setAccLabel2(Label accLabel2) {
        this.accLabel2 = accLabel2;
    }

    public void setPowerLabel2(Label powerLabel2) {
        this.powerLabel2 = powerLabel2;
    }

    public void setAttackNameLabel3(Label attackNameLabel3) {
        this.attackNameLabel3 = attackNameLabel3;
    }

    public void setAttackTypeLabel3(Label attackTypeLabel3) {
        this.attackTypeLabel3 = attackTypeLabel3;
    }

    public void setAccLabel3(Label accLabel3) {
        this.accLabel3 = accLabel3;
    }

    public void setPowerLabel3(Label powerLabel3) {
        this.powerLabel3 = powerLabel3;
    }

    public void setAttackNameLabel4(Label attackNameLabel4) {
        this.attackNameLabel4 = attackNameLabel4;
    }

    public void setAttackTypeLabel4(Label attackTypeLabel4) {
        this.attackTypeLabel4 = attackTypeLabel4;
    }

    public void setAccLabel4(Label accLabel4) {
        this.accLabel4 = accLabel4;
    }

    public void setPowerLabel4(Label powerLabel4) {
        this.powerLabel4 = powerLabel4;
    }


}
