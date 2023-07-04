package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EncounterController extends Controller {

    @FXML
    VBox attackBox1;
    @FXML
    VBox attackBox2;
    @FXML
    VBox attackBox3;
    @FXML
    VBox attackBox4;
    @FXML
    VBox enemyBeastInfo;
    @FXML
    Button leaveEncounter;
    @FXML
    Button changeMonster;
    @FXML
    VBox actionInfoBox;
    @FXML
    TextArea actionInfoText;
    @FXML
    VBox beastInfoBox;
    @FXML
    HBox enemyMonstersBox;
    @FXML
    HBox ownMonstersBox;
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
    Provider<EnemyBeastInfoController> enemyBeastInfoControllerProvider;
    @Inject
    Provider<BeastInfoController> beastInfoControllerProvider;
    @Inject
    Provider<RenderBeastController> renderBeastControllerProvider;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    Prefs prefs;

    //monster on the substitute's bench
    @SuppressWarnings("FieldCanBeLocal")
    private List<Monster> ownMonsters = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private List<Monster> allyMonsters = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private List<Monster> enemyMonsters = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private List<Monster> enemyAllyMonsters = new ArrayList<>();

    //monsters in the fight
    private Monster ownMonster;
    private Monster allyMonster;
    private Monster enemyMonster;
    private Monster enemyAllyMonster;

    @SuppressWarnings("FieldCanBeLocal")
    private Trainer allyTrainer;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Trainer enemyTrainer;
    @SuppressWarnings("FieldCanBeLocal")
    private Trainer enemyAllyTrainer;

    EnemyBeastInfoController enemyBeastInfoController1;
    EnemyBeastInfoController enemyBeastInfoController2;
    RenderBeastController renderBeastController1;
    RenderBeastController renderBeastController2;
    BeastInfoController beastInfoController1;
    BeastInfoController beastInfoController2;

    /*Trainer trainer = new Trainer(null, null, "1", "1", "user", "name", null, null,
            1, null, 1, 1, 1, null);
    Monster monster1 = new Monster(null, null,
            "1", "1", 3, 1, 0, new MonsterAttributes(100, 100, 100, 100),
            new MonsterAttributes(20, 10, 10, 5), Map.ofEntries(Map.entry("test1", 1), Map.entry("test2", 2), Map.entry("test3", 3)));
    Monster monster2 = new Monster(null, null,
            "1", "1", 1, 1, 0, new MonsterAttributes(100, 100, 100, 100),
            new MonsterAttributes(20, 10, 10, 5), Map.ofEntries(Map.entry("1", 1), Map.entry("2", 2)));
    Monster monster3 = new Monster(null, null,
            "1", "1", 2, 1, 0, new MonsterAttributes(100, 100, 100, 100),
            new MonsterAttributes(20, 10, 10, 5), Map.ofEntries(Map.entry("1", 1)));
    Monster monster4 = new Monster(null, null,
            "1", "1", 4, 1, 0, new MonsterAttributes(100, 100, 100, 100),
            new MonsterAttributes(20, 10, 10, 5), Map.ofEntries(Map.entry("1", 1), Map.entry("2", 2), Map.entry("3", 3)));*/


    @Inject
    public EncounterController() {
    }

    @Override
    public void init() {
        super.init();
        setOwnMonster(monster1);
        setAllyMonster(monster2);
        setEnemyMonster(monster3);
        setEnemyAllyMonster(monster4);

    }

    @Override
    public void onResize(int width, int height) {
        renderBeastController1.onResize(width, height);
        renderBeastController2.onResize(width, height);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        beastInfoController1 = beastInfoControllerProvider.get().setMonster(ownMonster);
        beastInfoBox.getChildren().addAll(beastInfoController1.render());
        renderBeastController1 = renderBeastControllerProvider.get().setMonster1(ownMonster);
        Parent ownMonster = renderBeastController1.render();
        ownMonstersBox.getChildren().addAll(ownMonster);
        HBox.setHgrow(ownMonster, Priority.ALWAYS);

        if (allyMonster != null) {
            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
            beastInfoBox.getChildren().addAll(beastInfoController2.render());
            renderBeastController1.setMonster2(allyMonster);
            ownMonstersBox.getChildren().clear();
            Parent allyMonster = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(allyMonster);
            HBox.setHgrow(allyMonster, Priority.ALWAYS);
        }

        enemyBeastInfoController1 = enemyBeastInfoControllerProvider.get().setMonster(enemyMonster);
        enemyBeastInfo.getChildren().addAll(enemyBeastInfoController1.render());
        renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
        Parent enemyMonster = renderBeastController2.render();
        enemyMonstersBox.getChildren().addAll(enemyMonster);
        HBox.setHgrow(enemyMonster, Priority.ALWAYS);

        if (enemyAllyMonster != null) {
            enemyBeastInfoController2 = enemyBeastInfoControllerProvider.get().setMonster(enemyAllyMonster);
            enemyBeastInfo.getChildren().addAll(enemyBeastInfoController2.render());
            renderBeastController2.setMonster2(enemyAllyMonster);
            enemyMonstersBox.getChildren().clear();
            Parent enemyAlly = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyAlly);
            HBox.setHgrow(enemyAlly, Priority.ALWAYS);
        }

        setNumberOfAttacks();
        return parent;
    }

    public void setNumberOfAttacks() {
        //get no of possible attacks
        System.out.println("no of attacks: " + ownMonster.abilities().size());
        if (ownMonster.abilities().size() == 1) {
            attackBox2.setVisible(false);
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (ownMonster.abilities().size() == 2) {
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (ownMonster.abilities().size() == 3) {
            attackBox4.setVisible(false);
        }

    }

    //onClicked leave encounter button
    public void leaveEncounter() {
        //TODO: switch screen to map
        System.out.println("leave encounter");
    }

    //onClicked change monster button
    public void changeMonster() {
        //TODO: switch screen to monster selection
        System.out.println("change monster");
    }

    //setter methods for monsters
    public void setOwnMonster(Monster ownMonster) {
        this.ownMonster = ownMonster;
    }

    public void setAllyMonster(Monster allyMonster) {
        this.allyMonster = allyMonster;
    }

    public void setEnemyMonster(Monster enemyMonster) {
        this.enemyMonster = enemyMonster;
    }

    public void setEnemyAllyMonster(Monster enemyAllyMonster) {
        this.enemyAllyMonster = enemyAllyMonster;
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
