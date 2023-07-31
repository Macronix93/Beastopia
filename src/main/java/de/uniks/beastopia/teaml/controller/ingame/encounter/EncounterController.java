package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.rest.Result;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.sockets.EventListener;
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
import java.util.Optional;
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
    Provider<ChangeBeastController> changeBeastControllerProvider;
    @Inject
    Provider<EndScreenController> endScreenControllerProvider;
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
    @Inject
    EventListener eventListener;

    //monster on the substitute's bench
    @SuppressWarnings({"FieldCanBeLocal"})
    private List<Monster> ownMonsters = new ArrayList<>();
    private final List<Monster> allyMonsters = new ArrayList<>();
    @SuppressWarnings({"FieldCanBeLocal"})
    private List<Monster> enemyMonsters = new ArrayList<>();
    private final List<Monster> enemyAllyMonsters = new ArrayList<>();

    //monsters in the fight
    private Monster myMonster;
    private Monster allyMonster;
    private Monster enemyMonster;
    private Monster enemyAllyMonster;

    private Trainer myTrainer;
    @SuppressWarnings({"FieldCanBeLocal"})
    private Trainer allyTrainer;
    private Trainer enemyTrainer;
    @SuppressWarnings({"FieldCanBeLocal"})
    private Trainer enemyAllyTrainer;

    private boolean shouldUpdateUIOnChange = false;
    private boolean clearActionBox = false;
    private String chosenTarget = null;
    //private String chosenMonster = null;
    Parent myMonsterParent;
    Parent allyMonsterParent;
    Parent enemyMonsterParent;
    Parent enemyAllyMonsterParent;
    Parent myMonsterInfo;
    Parent allyMonsterInfo;
    Parent enemyMonsterInfo;
    Parent enemyAllyMonsterInfo;

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

    @Inject
    public EncounterController() {
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

        if (myTrainer != null) {
            System.out.println("my trainer is not null!");

            beastInfoController1 = beastInfoControllerProvider.get().setMonster(myMonster);
            myMonsterInfo = beastInfoController1.render();
            beastInfoBox.getChildren().addAll(myMonsterInfo);
            renderBeastController1 = renderBeastControllerProvider.get().setMonster1(myMonster);
            renderBeastController1.setEncounterController(this);
            myMonsterParent = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(myMonsterParent);
            HBox.setHgrow(myMonsterParent, Priority.ALWAYS);

            // Set my monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(beastInfoController1.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> {
                        renderBeastController1.setMonsterOneOpponentId(opponent._id());
                        chosenTarget = opponent._id();
                    });

            // Add monster listener for my monster
            disposables.add(eventListener.listen("trainers." + cache.getTrainer()._id() + ".monsters." + myMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        beastInfoController1.hpLabel.setText(monsterEvent.data().currentAttributes().health() + " / " + monsterEvent.data().attributes().health() + " (HP)");
                        beastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health());

                        //updateUIOnChange();
                    }));
        }

        if (allyTrainer != null) {
            System.out.println("ally monster is not null!");

            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
            allyMonsterInfo = beastInfoController2.render();
            beastInfoBox.getChildren().addAll(allyMonsterInfo);
            renderBeastController1.setMonster2(allyMonster);
            ownMonstersBox.getChildren().clear();
            allyMonsterParent = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(allyMonsterParent);
            HBox.setHgrow(allyMonsterParent, Priority.ALWAYS);

            // Set ally monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(beastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id()));

            // Add monster listener for ally
            disposables.add(eventListener.listen("trainers." + allyTrainer._id() + ".monsters." + this.allyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        beastInfoController2.hpLabel.setText(monsterEvent.data().currentAttributes().health() + " / " + monsterEvent.data().attributes().health() + " (HP)");
                        beastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health());

                        //updateUIOnChange();
                    }));
        }

        if (enemyTrainer != null || cache.getCurrentEncounter().isWild()) {
            System.out.println("enemy monster is not null!");

            enemyBeastInfoController1 = enemyBeastInfoControllerProvider.get().setMonster(enemyMonster);
            enemyMonsterInfo = enemyBeastInfoController1.render();
            enemyBeastInfo.getChildren().addAll(enemyMonsterInfo);
            renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
            renderBeastController2.setEncounterController(this);
            enemyMonsterParent = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyMonsterParent);
            HBox.setHgrow(enemyMonsterParent, Priority.ALWAYS);

            // Set enemy monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(enemyBeastInfoController1.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController2.setMonsterOneOpponentId(opponent._id()));

            // Add monster listener for enemy
            disposables.add(eventListener.listen("trainers." + (cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id())) + ".monsters." + this.enemyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        enemyBeastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health());
                        //updateUIOnChange();
                    }));
        }

        if (enemyAllyTrainer != null) {
            System.out.println("enemy ally trainer not null!");

            // TODO: Debug monsters being the same
            System.out.println(enemyMonster);
            System.out.println(enemyAllyMonster);

            enemyBeastInfoController2 = enemyBeastInfoControllerProvider.get().setMonster(enemyAllyMonster);
            enemyAllyMonsterInfo = enemyBeastInfoController2.render();
            enemyBeastInfo.getChildren().addAll(enemyAllyMonsterInfo);
            renderBeastController2.setMonster2(enemyAllyMonster);
            enemyMonstersBox.getChildren().clear();
            enemyAllyMonsterParent = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyAllyMonsterParent);
            HBox.setHgrow(enemyAllyMonsterParent, Priority.ALWAYS);

            // Add monster listener for enemy ally
            disposables.add(eventListener.listen("trainers." + enemyAllyTrainer._id() + ".monsters." + enemyAllyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                                enemyBeastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health());
                                //updateUIOnChange();
                            }
                    ));

            // Set enemy ally monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(enemyBeastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> {
                        System.out.println("found opponent id: " + opponent._id());
                        renderBeastController2.setMonsterTwoOpponentId(opponent._id());
                    });
        }

        setNumberOfAttacks(myMonster);

        if (shouldUpdateUIOnChange) {
            updateUIOnChange();
        }

        if (!cache.getCurrentEncounter().isWild()) {
            leaveEncounter.setVisible(false);
        }

        disposables.add(eventListener.listen("encounters." + cache.getCurrentEncounter()._id() + ".trainers.*.opponents.*.*", Opponent.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                            if (o.suffix().equals("created")) {
                                cache.addCurrentOpponent(o.data());

                                allyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
                                allyTrainer = trainerService.getTrainer(cache.getJoinedRegion()._id(), o.data().trainer()).blockingFirst();

                                beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
                                allyMonsterInfo = beastInfoController2.render();
                                beastInfoBox.getChildren().addAll(allyMonsterInfo);
                                renderBeastController1.setMonster2(allyMonster);
                                ownMonstersBox.getChildren().clear();
                                allyMonsterParent = renderBeastController1.render();
                                ownMonstersBox.getChildren().addAll(allyMonsterParent);
                                HBox.setHgrow(allyMonsterParent, Priority.ALWAYS);

                                // Set ally monster opponent ID
                                cache.getCurrentOpponents().stream()
                                        .filter(opponent -> opponent.monster().equals(beastInfoController2.getMonster()._id()))
                                        .findFirst()
                                        .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id()));

                                // Add monster listener for ally
                                disposables.add(eventListener.listen("trainers." + allyTrainer._id() + ".monsters." + allyMonster._id() + ".updated", Monster.class)
                                        .observeOn(FX_SCHEDULER)
                                        .subscribe(monsterEvent -> {
                                            beastInfoController2.hpLabel.setText(monsterEvent.data().currentAttributes().health() + " / " + monsterEvent.data().attributes().health() + " (HP)");
                                            beastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health());

                                            //updateUIOnChange();
                                        }));
                            } else if (o.suffix().equals("updated")) {
                                if (o.data() != null) {
                                    String opponentId = o.data()._id();
                                    List<Result> results = o.data().results();
                                    String prefix = o.data().trainer().equals(cache.getTrainer()._id()) ? "My " : "Enemy ";

                                    if (o.data().trainer().equals(cache.getTrainer()._id())) {
                                        attackBox1.setDisable(false);
                                        attackBox2.setDisable(false);
                                        attackBox3.setDisable(false);
                                        attackBox4.setDisable(false);
                                    }

                                    if (renderBeastController2.getOpponentIdMonsterOne().equals(opponentId)) {
                                        // Update enemy monster in slot one, if it's not the same anymore
                                        if (!enemyBeastInfoController1.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                // TODO: unsubscribe from previous event and subscribe to new event
                                                System.out.println(o.data().monster());

                                                Monster newEnemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
                                                enemyBeastInfoController1.setMonster(newEnemyMonster);
                                                enemyBeastInfoController1.setLevel(newEnemyMonster.level());
                                                enemyBeastInfoController1.setLifeBarValue(newEnemyMonster.currentAttributes().health() / (double) newEnemyMonster.attributes().health());
                                                disposables.add(presetsService.getMonsterType(newEnemyMonster.type())
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterType -> {
                                                            enemyBeastInfoController1.setName(monsterType.name());
                                                            renderBeastController2.setImageMonsterOne(presetsService.getMonsterImage(monsterType.id()).blockingFirst());

                                                            // Get trainers name
                                                            String enemyTrainerName = null;
                                                            if (o.data().trainer().equals(enemyTrainer._id())) {
                                                                enemyTrainerName = enemyTrainer.name();
                                                            } else if (o.data().trainer().equals(enemyAllyTrainer._id())) {
                                                                enemyTrainerName = enemyAllyTrainer.name();
                                                            }
                                                            actionInfoText.appendText(enemyTrainerName + " sent out new beast " + monsterType.name() + ".\n");
                                                        }));
                                                renderBeastController2.setMonsterOneOpponentId(opponentId);
                                                renderBeastController2.setMonster1(newEnemyMonster);
                                                enemyMonster = newEnemyMonster;

                                                disposables.add(eventListener.listen("trainers." + enemyTrainer._id() + ".monsters." + newEnemyMonster._id() + ".updated", Monster.class)
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterEvent -> enemyBeastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health())));
                                                System.out.println("new monster: " + enemyMonster._id());
                                            }
                                        }
                                    }

                                    if (results == null && o.data().trainer().equals(cache.getTrainer()._id()) && o.data().move() == null) {
                                        attackBox1.setDisable(false);
                                        attackBox2.setDisable(false);
                                        attackBox3.setDisable(false);
                                        attackBox4.setDisable(false);
                                    }

                                    // Clear action info box for new events that happened
                                    if (clearActionBox) {
                                        actionInfoText.clear();
                                        clearActionBox = false;
                                    }

                                    cache.updateCurrentOpponents(o.data());
                                    String monsterName = getMonsterName(o.data().monster());
                                    if (monsterName != null) {
                                        for (Result result : results) {
                                            switch (result.type()) {
                                                case "ability-success" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". It was " + result.effectiveness() + ".\n");
                                                case "ability-failed" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". It failed due to status!\n");
                                                case "ability-no-uses" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". There are no ability points left!\n");
                                                case "target-defeated" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster()) + " was defeated!\n");
                                                case "status-added" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster()) + " got " + result.status() + "!\n");
                                            }
                                            if (result.status() != null) {
                                                actionInfoText.appendText(prefix + getMonsterName(result.monster()) + " is " + result.status() + "!\n");
                                            }
                                        }
                                    }
                                }
                            } else if (o.suffix().equals("deleted")) {
                                System.out.println("opponent deleted: " + o.data());
                                cache.removeOpponent(o.data()._id());
                                System.out.println(cache.getCurrentOpponents().size());

                                updateUIOnChange();

                                if (cache.getCurrentOpponents().size() > 1) {
                                    if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(o.data()._id())) {
                                        beastInfoBox.getChildren().remove(myMonsterInfo);
                                        beastInfoController1.destroy();
                                        renderBeastController1.setMonster1(null);
                                        renderBeastController1.setImageMonsterOne(null);
                                        chosenTarget = renderBeastController1.getOpponentIdMonsterTwo();
                                        System.out.println("remove my monster");
                                        // myMonster = null;
                                        myTrainer = null;
                                    } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(o.data()._id())) {
                                        beastInfoBox.getChildren().remove(allyMonsterInfo);
                                        beastInfoController2.destroy();
                                        renderBeastController1.setMonster2(null);
                                        renderBeastController1.setImageMonsterTwo(null);
                                        chosenTarget = renderBeastController1.getOpponentIdMonsterOne();
                                        System.out.println("remove ally monster");
                                        //allyMonster = null;
                                        allyTrainer = null;
                                    } else if (renderBeastController2.getOpponentIdMonsterOne() != null && renderBeastController2.getOpponentIdMonsterOne().equals(o.data()._id())) {
                                        enemyBeastInfo.getChildren().remove(enemyMonsterInfo);
                                        enemyBeastInfoController1.destroy();
                                        renderBeastController2.setMonster1(null);
                                        renderBeastController2.setImageMonsterOne(null);
                                        System.out.println("remove enemy");
                                        //enemyMonster = null;
                                        enemyTrainer = null;
                                    } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(o.data()._id())) {
                                        enemyBeastInfo.getChildren().remove(enemyAllyMonsterInfo);
                                        enemyBeastInfoController2.destroy();
                                        renderBeastController2.setMonster2(null);
                                        renderBeastController2.setImageMonsterTwo(null);
                                        System.out.println("remove enemy ally");
                                        //enemyAllyMonster = null;
                                        enemyAllyTrainer = null;
                                    }
                                }
                            }
                        },
                        error -> System.err.println("Fehler: " + error.getMessage()))
        );

        return parent;
    }

    private String getMonsterName(String id) {
        if (beastInfoController1.getMonster()._id().equals(id)) {
            return beastInfoController1.getName();
        } else if (allyMonster != null && beastInfoController2.getMonster()._id().equals(id)) {
            return beastInfoController2.getName();
        } else if (enemyBeastInfoController1.getMonster()._id().equals(id)) {
            return enemyBeastInfoController1.getName();
        } else if (enemyAllyMonster != null && enemyBeastInfoController2.getMonster()._id().equals(id)) {
            return enemyBeastInfoController2.getName();
        }
        return null;
    }

    private void setNumberOfAttacks(Monster monster) {
        attackBox1.setVisible(true);
        attackBox2.setVisible(true);
        attackBox3.setVisible(true);
        attackBox4.setVisible(true);

        if (monster.abilities().size() == 1) {
            attackBox2.setVisible(false);
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (monster.abilities().size() == 2) {
            attackBox3.setVisible(false);
            attackBox4.setVisible(false);
        } else if (monster.abilities().size() == 3) {
            attackBox4.setVisible(false);
        }
        setAttackBoxes(monster.abilities().size(), monster);
    }

    private void setAttackBoxes(int size, Monster monster) {
        Stack<Integer> stack = new Stack<>();
        Set<String> keys = monster.abilities().keySet();
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
        if (cache.getCurrentEncounter().isWild()) {
            disposables.add(
                    encounterOpponentsService.deleteOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getOpponentByTrainerID(cache.getTrainer()._id())._id())
                            .observeOn(FX_SCHEDULER)
                            .doFinally(() -> {
                                cache.setCurrentEncounter(null);
                                cache.getCurrentOpponents().clear();

                                IngameController controller = ingameControllerProvider.get();
                                controller.setRegion(cache.getJoinedRegion());
                                app.show(controller);
                            })
                            .subscribe()
            );
        }
    }

    //onClicked change monster button
    @FXML
    public void changeMonster() {
        ChangeBeastController controller = changeBeastControllerProvider.get();
        controller.setCurrentMonster(myMonster == null ? allyMonster : myMonster);
        controller.setEncounterController(this);
        app.show(controller);
    }

    // Trying to update UI when monster changed
    public void setToUpdateUIOnChange() {
        this.shouldUpdateUIOnChange = true;
    }

    //setter methods for monsters
    public EncounterController setOwnMonsters(List<Monster> ownMonsters) {
        this.ownMonsters = ownMonsters;
        return this;
    }

    public EncounterController setOwnMonster(Monster ownMonster) {
        this.myMonster = ownMonster;
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

    public EncounterController setMyTrainer(Trainer myTrainer) {
        this.myTrainer = myTrainer;
        return this;
    }

    public EncounterController setAllyTrainer(Trainer allyTrainer) {
        this.allyTrainer = allyTrainer;
        return this;
    }

    public EncounterController setEnemyTrainer(Trainer enemyTrainer) {
        this.enemyTrainer = enemyTrainer;
        return this;
    }

    public EncounterController setEnemyAllyTrainer(Trainer enemyAllyTrainer) {
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
        attackBox1.setDisable(true);
        attackBox2.setDisable(true);
        attackBox3.setDisable(true);
        attackBox4.setDisable(true);

        System.out.println(abilityDto.toString());
        System.out.println(cache.getOpponentByTrainerID(cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id())).toString());
        Monster before = myMonster;
        Monster beforeEnemy = enemyMonster;

        System.out.println(chosenTarget);

        disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                        cache.getCurrentEncounter()._id(), chosenTarget, null,
                        new AbilityMove("ability", abilityDto.id(), (cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id()))))
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        //e -> updateUIOnChange()
                ));
    }

    public void updateUIOnChange() {
        // Get the monster from the current opponents of the encounter
        disposables.add(encounterOpponentsService.getEncounterOpponents(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    // When there is no opponent registered on the server anymore = lose
                    if (o.size() == 0) {
                        //beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
                        //beastInfoController1.setLifeBarValue(0);

                        EndScreenController controller = endScreenControllerProvider.get();
                        controller.setWinner(false);
                        controller.setLoserMonster1(myMonster);
                        if (allyMonster != null) {
                            controller.setLoserMonster2(allyMonster);
                        }
                        controller.setWinnerMonster1(enemyMonster);
                        if (enemyAllyMonster != null) {
                            controller.setWinnerMonster2(enemyAllyMonster);
                        }
                        app.show(controller);
                    } else {
                        for (Opponent opponent : o) {
                            // Check if the opponent is our trainers id
                            if (opponent.trainer().equals(cache.getTrainer()._id())) {
                                // If the monster has died during change, show 0 HP, otherwise the current HP of the monster
                                if (opponent.monster() != null) {
                                    myMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), opponent.monster()).blockingFirst();
                                    beastInfoController1.hpLabel.setText(myMonster.currentAttributes().health() + " / " + myMonster.attributes().health() + " (HP)");
                                    beastInfoController1.setLifeBarValue(myMonster.currentAttributes().health() / (double) myMonster.attributes().health());
                                } else {
                                    //beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
                                    //beastInfoController1.setLifeBarValue(0);

                                    List<Monster> currentMons = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), cache.getTrainer()._id()).blockingFirst();

                                    // Check if there are still mons with hp left in my trainers team
                                    Optional<Monster> optionalMyMonster = currentMons.stream()
                                            .filter(m -> m.currentAttributes().health() > 0 && !m._id().equals(myMonster._id()))
                                            .findFirst();

                                    // If not, show lose screen
                                    if (optionalMyMonster.isEmpty()) {
                                        EndScreenController controller = endScreenControllerProvider.get();
                                        controller.setWinner(false);
                                        controller.setLoserMonster1(myMonster);
                                        if (allyMonster != null) {
                                            controller.setLoserMonster2(allyMonster);
                                        }
                                        controller.setWinnerMonster1(enemyMonster);
                                        if (enemyAllyMonster != null) {
                                            controller.setWinnerMonster2(enemyAllyMonster);
                                        }
                                        app.show(controller);
                                    }
                                }
                            } else {
                                if (opponent.monster() != null) {
                                    //enemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : enemyTrainer._id(), opponent.monster()).blockingFirst();
                                    //enemyBeastInfoController1.setLifeBarValue((double) enemyMonster.currentAttributes().health() / (double) enemyMonster.attributes().health());

                                    // Check if enemy still has a monster with hp left
                                    if (!cache.getCurrentEncounter().isWild()) {
                                        enemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), (cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id())), opponent.monster()).blockingFirst();
                                        enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), opponent.trainer()).blockingFirst();

                                        boolean foundMonster = false;
                                        for (Monster monster : enemyMonsters) {
                                            if (!monster._id().equals(enemyMonster._id()) && monster.currentAttributes().health() > 0) {
                                                foundMonster = true;
                                                break;
                                            }
                                        }
                                        if (enemyMonster.currentAttributes().health() <= 0 && !foundMonster && (enemyAllyMonster == null || enemyAllyMonster.currentAttributes().health() <= 0)) {
                                            EndScreenController controller = endScreenControllerProvider.get();
                                            controller.setWinner(true);
                                            controller.setLoserMonster1(enemyMonster);
                                            if (enemyAllyMonster != null) {
                                                controller.setLoserMonster2(enemyAllyMonster);
                                            }
                                            controller.setWinnerMonster1(myMonster);
                                            if (allyMonster != null) {
                                                controller.setWinnerMonster2(allyMonster);
                                            }
                                            app.show(controller);
                                        }
                                    }
                                } else {
                                    EndScreenController controller = endScreenControllerProvider.get();
                                    controller.setWinner(true);
                                    controller.setLoserMonster1(enemyMonster);
                                    if (enemyAllyMonster != null) {
                                        controller.setLoserMonster2(enemyAllyMonster);
                                    }
                                    controller.setWinnerMonster1(myMonster);
                                    if (allyMonster != null) {
                                        controller.setWinnerMonster2(allyMonster);
                                    }
                                    app.show(controller);
                                }
                            }
                        }
                    }
                }));
    }

    public void setChosenTarget(String opponentId) {
        onUI(() -> {
            if (chosenTarget != null && !chosenTarget.equals(opponentId)) {
                chosenTarget = opponentId;
                if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponentId)) {
                    setNumberOfAttacks(beastInfoController1.getMonster());
                    //this.chosenMonster = beastInfoController1.getMonster()._id();
                } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId)) {
                    setNumberOfAttacks(beastInfoController2.getMonster());
                    //this.chosenMonster = beastInfoController2.getMonster()._id();
                }
            }
        });
    }
}