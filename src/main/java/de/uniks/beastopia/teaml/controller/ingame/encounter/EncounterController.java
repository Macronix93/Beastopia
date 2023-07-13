package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
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
import java.util.Set;
import java.util.Stack;

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
    Provider<IngameController> ingameControllerProvider;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    Prefs prefs;

    //monster on the substitute's bench
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private List<Monster> ownMonsters = new ArrayList<>();
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private List<Monster> allyMonsters = new ArrayList<>();
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private List<Monster> enemyMonsters = new ArrayList<>();
    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private List<Monster> enemyAllyMonsters = new ArrayList<>();

    //monsters in the fight
    private Monster ownMonster;
    private Monster allyMonster;
    private Monster enemyMonster;
    private Monster enemyAllyMonster;

    @SuppressWarnings("FieldCanBeLocal")
    private String allyTrainer;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String enemyTrainer;
    @SuppressWarnings("FieldCanBeLocal")
    private String enemyAllyTrainer;

    private AbilityDto ability1;
    private AbilityDto ability2;
    private AbilityDto ability3;
    private AbilityDto ability4;
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
        /*setOwnMonster(monster1);
        setAllyMonster(monster2);
        setEnemyMonster(monster3);
        setEnemyAllyMonster(monster4);*/
        // this.ownMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), cache.getTrainer().team().get(0)).blockingFirst();
    }

    @Override
    public void onResize(int width, int height) {
        if (renderBeastController1 != null)
            renderBeastController1.onResize(width, height);
        if (renderBeastController2 != null)
            renderBeastController2.onResize(width, height);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        showOwnMonster(ownMonster);

        if (allyMonster != null) {
            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
            beastInfoBox.getChildren().addAll(beastInfoController2.render());
            renderBeastController1.setMonster2(allyMonster);
            ownMonstersBox.getChildren().clear();
            Parent allyMonster = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(allyMonster);
            HBox.setHgrow(allyMonster, Priority.ALWAYS);
        }

        showEnemyMonster(enemyMonster);

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

    private void showOwnMonster(Monster ownMonster) {
        beastInfoController1 = beastInfoControllerProvider.get().setMonster(ownMonster);
        beastInfoBox.getChildren().addAll(beastInfoController1.render());
        renderBeastController1 = renderBeastControllerProvider.get().setMonster1(ownMonster);
        Parent parent = renderBeastController1.render();
        ownMonstersBox.getChildren().addAll(parent);
        HBox.setHgrow(parent, Priority.ALWAYS);
    }

    private void showEnemyMonster(Monster enemyMonster) {
        enemyBeastInfoController1 = enemyBeastInfoControllerProvider.get().setMonster(enemyMonster);
        enemyBeastInfo.getChildren().addAll(enemyBeastInfoController1.render());
        renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
        Parent parent = renderBeastController2.render();
        enemyMonstersBox.getChildren().addAll(parent);
        HBox.setHgrow(parent, Priority.ALWAYS);
    }

    private void setNumberOfAttacks() {
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
        setAttackBoxes(ownMonster.abilities().size());
    }

    private void setAttackBoxes(int size) {
        Stack<Integer> stack = new Stack<>();
        Set<String> keys = ownMonster.abilities().keySet();
        keys.forEach(key -> stack.push(Integer.parseInt(key)));
        switch (size) {
            case 4:
                setAttack4(presetsService.getAbility(stack.pop()).blockingFirst());
            case 3:
                setAttack3(presetsService.getAbility(stack.pop()).blockingFirst());
            case 2:
                setAttack2(presetsService.getAbility(stack.pop()).blockingFirst());
            case 1:
                setAttack1(presetsService.getAbility(stack.pop()).blockingFirst());
        }
    }

    //onClicked leave encounter button
    @FXML
    public void leaveEncounter() {
        //TODO: switch screen to map
        System.out.println("leave encounter");

        if (cache.getCurrentEncounter().isWild()) {
            System.out.println("current encounter: " + cache.getCurrentEncounter() + " current opponent: " + cache.getCurrentOpponents().get(1));

            disposables.add(encounterOpponentsService.deleteOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getCurrentOpponents().get(1)._id()).subscribe());

            cache.setCurrentEncounter(null);
            cache.getCurrentOpponents().clear();

            IngameController controller = ingameControllerProvider.get();
            controller.setRegion(cache.getJoinedRegion());
            app.show(controller);
        }
    }

    //onClicked change monster button
    @FXML
    public void changeMonster() {
        //TODO: switch screen to monster selection
        System.out.println("change monster");
    }

    //setter methods for monsters
    public EncounterController setOwnMonsters(List<Monster> ownMonsters) {
        this.ownMonsters = ownMonsters;
        return this;
    }

    public EncounterController setOwnMonster(Monster ownMonster) {
        this.ownMonster = ownMonster;
        return this;
    }

    public EncounterController setEnemyMonsters(List<Monster> enemyMonsters) {
        this.enemyMonsters = enemyMonsters;
        return this;
    }

    public EncounterController setAllyMonster(Monster allyMonster) {
        this.allyMonster = allyMonster;
        return this;
    }

    public EncounterController setEnemyMonster(Monster enemyMonster) {
        this.enemyMonster = enemyMonster;
        return this;
    }

    public EncounterController setEnemyAllyMonster(Monster enemyAllyMonster) {
        this.enemyAllyMonster = enemyAllyMonster;
        return this;
    }

    public EncounterController setAllyTrainer(String allyTrainer) {
        this.allyTrainer = allyTrainer;
        return this;
    }

    public EncounterController setEnemyTrainer(String enemyTrainer) {
        this.enemyTrainer = enemyTrainer;
        return this;
    }

    public EncounterController setEnemyAllyTrainer(String enemyAllyTrainer) {
        this.enemyAllyTrainer = enemyAllyTrainer;
        return this;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    //methods for attack buttons
    @FXML
    public void executeAttack1() {
        setAttackWithClick(attackBox1, ability1);
    }

    @FXML
    public void executeAttack2() {
        setAttackWithClick(attackBox2, ability2);
    }

    @FXML
    public void executeAttack3() {
        setAttackWithClick(attackBox3, ability3);
    }

    @FXML
    public void executeAttack4() {
        setAttackWithClick(attackBox4, ability4);
    }

    //methods for setting labels in attack boxes
    private void setAttack1(AbilityDto abilityDto) {
        attackNameLabel1.setText(abilityDto.name());
        attackTypeLabel1.setText("Type: " + abilityDto.type());
        accLabel1.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel1.setText("Power: " + abilityDto.power());
        ability1 = abilityDto;
    }

    private void setAttack2(AbilityDto abilityDto) {
        attackNameLabel2.setText(abilityDto.name());
        attackTypeLabel2.setText("Type: " + abilityDto.type());
        accLabel2.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel2.setText("Power: " + abilityDto.power());
        ability2 = abilityDto;
    }

    private void setAttack3(AbilityDto abilityDto) {
        attackNameLabel3.setText(abilityDto.name());
        attackTypeLabel3.setText("Type: " + abilityDto.type());
        accLabel3.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel3.setText("Power: " + abilityDto.power());
        ability3 = abilityDto;
    }

    private void setAttack4(AbilityDto abilityDto) {
        attackNameLabel4.setText(abilityDto.name());
        attackTypeLabel4.setText("Type: " + abilityDto.type());
        accLabel4.setText("Accuracy: " + abilityDto.accuracy());
        powerLabel4.setText("Power: " + abilityDto.power());
        ability4 = abilityDto;
    }

    private void setAttackWithClick(VBox attackBox, AbilityDto abilityDto) {
        System.out.println(abilityDto.toString());
        System.out.println(cache.getOpponent(enemyTrainer).toString());
        Monster before = ownMonster;
        Monster beforeEnemy = enemyMonster;
        disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                        cache.getCurrentEncounter()._id(), cache.getOpponent(cache.getTrainer()._id())._id(), null
                        , new AbilityMove("ability", abilityDto.id(), cache.getOpponent(enemyTrainer)._id()))
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        e -> {
                            showOwnMonster(ownMonster);
                            showEnemyMonster(enemyMonster); //TODO Update?
                            if (ownMonster.currentAttributes().health() <= 0) {
                                setNumberOfAttacks(); //Update AttackBoxes if changed beast or monster died
                            }
                        }
                ));
    }
}
