package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EncounterController extends Controller {

    @FXML
    private VBox attackBox1;
    @FXML
    private VBox attackBox2;
    @FXML
    private VBox attackBox3;
    @FXML
    private VBox attackBox4;
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
    PresetsService presetsService;
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

    @FXML
    ImageView test;

    Trainer trainer = new Trainer(null, null, "1", "1", "user", "name", null,
            1, null, 1, 1, 1, null);
    Monster monster = new Monster(null, null,
            "1", "jor",3, 1, 10, null, null);

    @Inject
    public EncounterController() {
    }

    @Override
    public void init() {

        super.init();
        setFightMode();
        System.out.println("jor");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(presetsService.getMonsterImage(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> test.setImage(monsterImage)));

        getOwnMonsters();
        if (enemyTrainer != null) {
            getEnemyTrainerMonsters();
        } else {
            getEnemyMonster();
        }

        return parent;
    }

    //TODO: set no of possible attacks according to active monster, unneeded boxes must be set to invisible
    public void setNumberOfAttacks() {

    }

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
        /*
        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    this.ownMonsters.addAll(monsters);
                }));*/
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

    //methods for attack buttons
    public void executeAttack1() {
        System.out.println("attack1");
    }

    public void executeAttack2() {
        System.out.println("attack2");
    }

    public void executeAttack3() {
        System.out.println("attack3");
    }

    public void executeAttack4() {
        System.out.println("attack4");
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

    //methods for setting labels in attack boxes
    public void setAttackNameLabel1(String value) {
        attackNameLabel1.setText(value);
    }

    public void setAttackTypeLabel1(String value) {
        attackTypeLabel1.setText(value);
    }

    public void setAccLabel1(String value) {
        accLabel1.setText(value);
    }

    public void setPowerLabel1(String value) {
        powerLabel1.setText(value);
    }

    public void setAttackNameLabel2(String value) {
        attackNameLabel2.setText(value);
    }

    public void setAttackTypeLabel2(String value) {
        attackTypeLabel2.setText(value);
    }

    public void setAccLabel2(String value) {
        accLabel2.setText(value);
    }

    public void setPowerLabel2(String value) {
        powerLabel2.setText(value);
    }

    public void setAttackNameLabel3(String value) {
        attackNameLabel3.setText(value);
    }

    public void setAttackTypeLabel3(String value) {
        attackTypeLabel3.setText(value);
    }

    public void setAccLabel3(String value) {
        accLabel3.setText(value);
    }

    public void setPowerLabel3(String value) {
        powerLabel3.setText(value);
    }

    public void setAttackNameLabel4(String value) {
        attackNameLabel4.setText(value);
    }

    public void setAttackTypeLabel4(String value) {
        attackTypeLabel4.setText(value);
    }

    public void setAccLabel4(String value) {
        accLabel4.setText(value);
    }

    public void setPowerLabel4(String value) {
        powerLabel4.setText(value);
    }


}
