package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.controller.ingame.items.InventoryController;
import de.uniks.beastopia.teaml.controller.ingame.items.ItemDetailController;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.Event;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public class EncounterController extends Controller {
    @FXML
    public VBox inventoryLayout;
    @FXML
    public HBox itemBox;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public AnchorPane infoAnchorPane;
    @FXML
    public VBox catchInfoBox;
    @FXML
    StackPane stack;
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
    public TextArea actionInfoText;
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
    Provider<InventoryController> inventoryControllerProvider;
    @Inject
    Provider<ChangeBeastController> changeBeastControllerProvider;
    @Inject
    Provider<EndScreenController> endScreenControllerProvider;
    @Inject
    Provider<LevelUpController> levelUpControllerProvider;
    @Inject
    Provider<ItemDetailController> itemDetailControllerProvider;
    @Inject
    Provider<CatchInfoController> catchInfoControllerProvider;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    InventoryController inventoryController;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    Prefs prefs;
    @Inject
    EventListener eventListener;
    //monster on the substitute's bench
    private List<Monster> ownMonsters = new ArrayList<>();
    private final List<Monster> allyMonsters = new ArrayList<>();
    private List<Monster> enemyMonsters = new ArrayList<>();
    private final List<Monster> enemyAllyMonsters = new ArrayList<>();
    private int oldLevel;
    private double oldHp;
    private final String wildTrainerId = "000000000000000000000000";
    //monsters in the fight
    Monster myMonster;
    Monster allyMonster;
    public Monster enemyMonster;
    Monster enemyAllyMonster;

    Trainer myTrainer;
    Trainer allyTrainer;
    Trainer enemyTrainer;
    Trainer enemyAllyTrainer;

    private boolean shouldUpdateUIOnChange = false;
    private String chosenTarget = null;
    private boolean hasToChooseEnemy = false;
    private boolean isOneVersusTwo;
    private AbilityDto chosenAbility;
    private ItemTypeDto lastItemTypeDto;
    Parent myMonsterParent;
    Parent allyMonsterParent;
    Parent enemyMonsterParent;
    Parent enemyAllyMonsterParent;
    Parent myMonsterInfo;
    Parent allyMonsterInfo;
    Parent enemyMonsterInfo;
    Parent enemyAllyMonsterInfo;
    Parent inventoryParent;
    Parent itemDetailParent;

    private float oldCoinNum;
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
    private boolean monBallUsed;
    public ItemTypeDto usedItemTypeDto;

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

        inventoryLayout.setVisible(false);
        oldCoinNum = cache.getTrainer().coins();
        AtomicInteger numberOfAttackers = new AtomicInteger((int) cache.getCurrentOpponents().stream().filter(Opponent::isAttacker).count());
        isOneVersusTwo = numberOfAttackers.get() == 2;

        if (myTrainer != null) {
            beastInfoController1 = beastInfoControllerProvider.get().setMonster(myMonster);
            myMonsterInfo = beastInfoController1.render();
            beastInfoBox.getChildren().addAll(myMonsterInfo);
            renderBeastController1 = renderBeastControllerProvider.get().setMonster1(myMonster);
            renderBeastController1.setEncounterController(this);
            myMonsterParent = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(myMonsterParent);
            HBox.setHgrow(myMonsterParent, Priority.ALWAYS);

            // Add monster listener and opponent id for my monster
            addMonsterListener(true, false, false, false);
        }

        if (allyTrainer != null) {
            initializeAllyBeastInfo();

            // Add monster listener for ally
            addMonsterListener(false, true, false, false);
        }

        if (enemyTrainer != null || cache.getCurrentEncounter().isWild()) {
            enemyBeastInfoController1 = enemyBeastInfoControllerProvider.get().setMonster(enemyMonster);
            enemyMonsterInfo = enemyBeastInfoController1.render();
            enemyBeastInfo.getChildren().addAll(enemyMonsterInfo);
            renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
            renderBeastController2.setEncounterController(this);
            enemyMonsterParent = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyMonsterParent);
            HBox.setHgrow(enemyMonsterParent, Priority.ALWAYS);

            // Add monster listener and opponent id for enemy
            addMonsterListener(false, false, true, false);
        }

        if (enemyAllyTrainer != null) {
            enemyBeastInfoController2 = enemyBeastInfoControllerProvider.get().setMonster(enemyAllyMonster);
            enemyAllyMonsterInfo = enemyBeastInfoController2.render();
            enemyBeastInfo.getChildren().addAll(enemyAllyMonsterInfo);
            renderBeastController2.setMonster2(enemyAllyMonster);
            enemyMonstersBox.getChildren().clear();
            enemyAllyMonsterParent = renderBeastController2.render();
            enemyMonstersBox.getChildren().addAll(enemyAllyMonsterParent);
            HBox.setHgrow(enemyAllyMonsterParent, Priority.ALWAYS);

            // Add monster listener and opponent id for enemy ally
            addMonsterListener(false, false, false, true);
        }

        setNumberOfAttacks(myMonster);

        if (shouldUpdateUIOnChange) {
            updateUIOnChange();
            shouldUpdateUIOnChange = false;
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

                                initializeAllyBeastInfo();

                                // Add monster listener and opponent id for ally
                                addMonsterListener(false, true, false, false);
                            } else if (o.suffix().equals("updated")) {
                                if (o.data() != null) {
                                    String opponentId = o.data()._id();
                                    List<Result> results = o.data().results();
                                    String prefix = o.data().trainer().equals(cache.getTrainer()._id()) ? "My " : "Enemy ";

                                    if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId) && !allyTrainer._id().equals(cache.getTrainer()._id())) {
                                        // Update ally monster if it's not our own
                                        if (!beastInfoController2.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                Monster newAllyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
                                                beastInfoController2.setLevel(newAllyMonster.level());
                                                beastInfoController2.setMonster(newAllyMonster);
                                                beastInfoController2.hpLabel.setText((int) newAllyMonster.currentAttributes().health() + " / " + (int) newAllyMonster.attributes().health() + " (HP)");
                                                beastInfoController2.setLifeBarValue(newAllyMonster.currentAttributes().health() / newAllyMonster.attributes().health(), false);
                                                beastInfoController2.setStatus(newAllyMonster.status(), false);

                                                if (cache.getAllBeasts().stream().noneMatch(type -> type.id() == newAllyMonster.type())) {
                                                    disposables.add(presetsService.getMonsterType(newAllyMonster.type())
                                                            .observeOn(FX_SCHEDULER)
                                                            .subscribe(monsterType -> {
                                                                cache.addToAllBeasts(monsterType);
                                                                beastInfoController2.setName(monsterType.name());
                                                                actionInfoText.appendText(allyTrainer.name() + " sent out new beast " + monsterType.name() + ".\n");
                                                            }));
                                                } else {
                                                    beastInfoController2.setName(cache.getBeastDto(newAllyMonster.type()).name());
                                                    actionInfoText.appendText(allyTrainer.name() + " sent out new beast " + cache.getBeastDto(newAllyMonster.type()).name() + ".\n");
                                                }
                                                if (!cache.imageIsDownloaded(newAllyMonster.type())) {
                                                    Image monsterImage = presetsService.getMonsterImage(newAllyMonster.type()).blockingFirst();
                                                    cache.addMonsterImages(newAllyMonster.type(), monsterImage);
                                                    renderBeastController1.setImageMonsterTwo(monsterImage);
                                                } else {
                                                    renderBeastController1.setImageMonsterTwo(cache.getMonsterImage(newAllyMonster.type()));
                                                }
                                                renderBeastController1.setMonsterOneOpponentId(opponentId);
                                                renderBeastController1.setMonster2(newAllyMonster);
                                                allyMonster = newAllyMonster;

                                                addMonsterListener(false, true, false, false);
                                            }
                                        }
                                    } else if (renderBeastController2.getOpponentIdMonsterOne().equals(opponentId)) {
                                        // Update enemy monster in slot one, if it's not the same anymore
                                        if (!enemyBeastInfoController1.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                Monster newEnemyMonster = getNewEnemyMonster(o, enemyBeastInfoController1, enemyTrainer);
                                                if (!cache.imageIsDownloaded(newEnemyMonster.type())) {
                                                    Image monsterImage = presetsService.getMonsterImage(newEnemyMonster.type()).blockingFirst();
                                                    cache.addMonsterImages(newEnemyMonster.type(), monsterImage);
                                                    renderBeastController2.setImageMonsterOne(monsterImage);
                                                } else {
                                                    renderBeastController2.setImageMonsterOne(cache.getMonsterImage(newEnemyMonster.type()));
                                                }
                                                renderBeastController2.setMonsterOneOpponentId(opponentId);
                                                renderBeastController2.setMonster1(newEnemyMonster);
                                                enemyMonster = newEnemyMonster;

                                                addMonsterListener(false, false, true, false);
                                            }
                                        }
                                    } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(opponentId)) {
                                        // Update enemy monster in slot two, if it's not the same anymore
                                        if (!enemyBeastInfoController2.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                Monster newEnemyAllyMonster = getNewEnemyMonster(o, enemyBeastInfoController2, enemyAllyTrainer);
                                                if (!cache.imageIsDownloaded(newEnemyAllyMonster.type())) {
                                                    Image monsterImage = presetsService.getMonsterImage(newEnemyAllyMonster.type()).blockingFirst();
                                                    cache.addMonsterImages(newEnemyAllyMonster.type(), monsterImage);
                                                    renderBeastController2.setImageMonsterTwo(monsterImage);
                                                } else {
                                                    renderBeastController2.setImageMonsterTwo(cache.getMonsterImage(newEnemyAllyMonster.type()));
                                                }
                                                renderBeastController2.setMonsterTwoOpponentId(opponentId);
                                                renderBeastController2.setMonster2(newEnemyAllyMonster);
                                                enemyAllyMonster = newEnemyAllyMonster;

                                                addMonsterListener(false, false, false, true);
                                            }
                                        }
                                    }

                                    // Update opponent in cache and show battle results
                                    cache.updateCurrentOpponents(o.data());
                                    String monsterName = getMonsterName(o.data().monster(), null);
                                    if (monsterName != null && results != null) {
                                        renderBeastController1.removeSelectBox();
                                        renderBeastController2.removeSelectBox();
                                        if (o.data().trainer().equals(cache.getTrainer()._id())) {
                                            setAttackBoxesDisabled(false);
                                        }
                                        for (Result result : results) {
                                            switch (result.type()) {
                                                case "ability-success" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + cache.getAbilities().computeIfAbsent(result.ability(), id -> presetsService.getAbility(id).blockingFirst()).name() + ". It was " + result.effectiveness() + ".\n");
                                                case "ability-failed" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + cache.getAbilities().computeIfAbsent(result.ability(), id -> presetsService.getAbility(id).blockingFirst()).name() + ". It failed due to status!\n");
                                                case "ability-no-uses" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + cache.getAbilities().computeIfAbsent(result.ability(), id -> presetsService.getAbility(id).blockingFirst()).name() + ". There are no ability points left!\n");
                                                case "target-defeated" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " was defeated!\n");
                                                case "status-added" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " got " + result.status() + "!\n");
                                                case "status-removed" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " status got removed!\n");
                                                case "status-damage" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " got status damage. It was " + result.effectiveness() + "!\n");
                                                case "target-unknown" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " missed the attack!\n");
                                                case "item-success" -> {
                                                    if (monBallUsed) {
                                                        setMonBallUsed(false);
                                                        setCatchInfoBox(false);
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " got out the ball!\n");
                                                        return;
                                                    } else {
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " successfully used an item!\n");
                                                    }
                                                }
                                                case "item-failed" ->
                                                        actionInfoText.appendText(getMonsterName(result.monster(), null) + " used an item, but it failed!\n");
                                            }
                                            if (result.status() != null && !result.type().equals("status-removed") && !result.type().equals("status-damage")) {
                                                actionInfoText.appendText(prefix + getMonsterName(result.monster(), null) + " is " + result.status() + "!\n");
                                            }
                                            if (result.type().contains("item")) {
                                                if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponentId)) {
                                                    showItemAnimation(renderBeastController1, renderBeastController1.selectBox, result.item());
                                                } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId)) {
                                                    showItemAnimation(renderBeastController1, renderBeastController1.selectBox2, result.item());
                                                } else if (renderBeastController2.getOpponentIdMonsterOne() != null && renderBeastController2.getOpponentIdMonsterOne().equals(opponentId)) {
                                                    showItemAnimation(renderBeastController2, renderBeastController2.selectBox, result.item());
                                                } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(opponentId)) {
                                                    showItemAnimation(renderBeastController2, renderBeastController2.selectBox2, result.item());
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (o.suffix().equals("deleted")) {
                                cache.removeOpponent(o.data()._id());
                                if (monBallUsed && o.data().trainer().equals(cache.getTrainer()._id())) {
                                    setMonBallUsed(false);
                                    setCatchInfoBox(true);
                                    return;
                                }
                                if (o.data().trainer().equals(cache.getTrainer()._id()) && allyTrainer != null && !allyTrainer._id().equals(cache.getTrainer()._id())) {
                                    if (myMonster.currentAttributes().health() == 0) {
                                        boolean foundMonsterWithHP = false;
                                        for (Monster monster : ownMonsters) {
                                            if (!monster._id().equals(myMonster._id()) && cache.getTrainer().team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                                                foundMonsterWithHP = true;
                                                break;
                                            }
                                        }
                                        if (!foundMonsterWithHP) {
                                            EndScreenController endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
                                            app.show(endScreenController);
                                        }
                                    }
                                } else {
                                    if (!monBallUsed) {
                                        updateUIOnChange();

                                        if (cache.getCurrentOpponents().size() > 1) {
                                            if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(o.data()._id())) {
                                                beastInfoBox.getChildren().remove(myMonsterInfo);
                                                beastInfoController1.destroy();
                                                renderBeastController1.setMonster1(null);
                                                renderBeastController1.setImageMonsterOne(null);
                                                chosenTarget = renderBeastController1.getOpponentIdMonsterTwo();
                                                setNumberOfAttacks(beastInfoController2.getMonster());
                                                myTrainer = null;
                                            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(o.data()._id())) {
                                                beastInfoBox.getChildren().remove(allyMonsterInfo);
                                                beastInfoController2.destroy();
                                                renderBeastController1.setMonster2(null);
                                                renderBeastController1.setImageMonsterTwo(null);
                                                chosenTarget = renderBeastController1.getOpponentIdMonsterOne();
                                                setNumberOfAttacks(beastInfoController1.getMonster());
                                                allyTrainer = null;
                                            } else if (renderBeastController2.getOpponentIdMonsterOne() != null && renderBeastController2.getOpponentIdMonsterOne().equals(o.data()._id())) {
                                                enemyBeastInfo.getChildren().remove(enemyMonsterInfo);
                                                enemyBeastInfoController1.destroy();
                                                renderBeastController2.setMonster1(null);
                                                renderBeastController2.setImageMonsterOne(null);
                                                enemyTrainer = null;
                                            } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(o.data()._id())) {
                                                enemyBeastInfo.getChildren().remove(enemyAllyMonsterInfo);
                                                enemyBeastInfoController2.destroy();
                                                renderBeastController2.setMonster2(null);
                                                renderBeastController2.setImageMonsterTwo(null);
                                                enemyAllyTrainer = null;
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        error -> System.err.println("Error: " + error.getMessage()))
        );

        return parent;
    }

    private Monster getNewEnemyMonster(Event<Opponent> o, EnemyBeastInfoController enemyBeastInfoController1, Trainer enemyTrainer) {
        Monster newEnemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
        enemyBeastInfoController1.setMonster(newEnemyMonster);
        enemyBeastInfoController1.setLevel(newEnemyMonster.level());
        enemyBeastInfoController1.setLifeBarValue(newEnemyMonster.currentAttributes().health() / newEnemyMonster.attributes().health(), false);
        enemyBeastInfoController1.setStatus(newEnemyMonster.status(), false);

        if (cache.getAllBeasts().stream().noneMatch(type -> type.id() == newEnemyMonster.type())) {
            disposables.add(presetsService.getMonsterType(newEnemyMonster.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterType -> {
                        cache.addToAllBeasts(monsterType);
                        enemyBeastInfoController1.setName(monsterType.name());
                        actionInfoText.appendText(enemyTrainer.name() + " sent out new beast " + monsterType.name() + ".\n");
                    }));
        } else {
            enemyBeastInfoController1.setName(cache.getBeastDto(newEnemyMonster.type()).name());
            actionInfoText.appendText(enemyTrainer.name() + " sent out new beast " + cache.getBeastDto(newEnemyMonster.type()).name() + ".\n");
        }
        return newEnemyMonster;
    }

    private Monster updateMonsterInfo(Event<Monster> monsterEvent, BeastInfoController beastInfoController, List<Monster> monsterList) {
        beastInfoController.hpLabel.setText((int) monsterEvent.data().currentAttributes().health() + " / " + (int) monsterEvent.data().attributes().health() + " (HP)");
        beastInfoController.setLifeBarValue(monsterEvent.data().currentAttributes().health() / monsterEvent.data().attributes().health(), false);
        beastInfoController.setStatus(monsterEvent.data().status(), false);
        Monster monster = monsterEvent.data();
        updateMonsterInList(monster, monsterList);
        return monster;
    }

    private void addMonsterListener(boolean myTrainer, boolean allyTrainer, boolean enemyTrainer, boolean enemyAllyTrainer) {
        if (myTrainer) {
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster() == null ? opponent.trainer().equals(cache.getTrainer()._id()) : opponent.monster().equals(beastInfoController1.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> {
                        renderBeastController1.setMonsterOneOpponentId(opponent._id());
                        chosenTarget = opponent._id();
                    });
            disposables.add(eventListener.listen("trainers." + cache.getTrainer()._id() + ".monsters." + myMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> myMonster = updateMonsterInfo(monsterEvent, beastInfoController1, ownMonsters)));
        } else if (allyTrainer) {
            // Set ally monster opponent ID
            if (this.allyTrainer._id().equals(cache.getTrainer()._id())) {
                // 2v2 situation
                if (renderBeastController1.getOpponentIdMonsterOne() != null) {
                    cache.getCurrentOpponents().stream().filter(o -> o.trainer().equals(this.allyTrainer._id()) && !o._id().equals(renderBeastController1.getOpponentIdMonsterOne()))
                            .findFirst()
                            .ifPresent(o -> renderBeastController1.setMonsterTwoOpponentId(o._id()));
                }
            } else {
                cache.getCurrentOpponents().stream().filter(opponent -> opponent.monster() == null ? opponent.trainer().equals(this.allyTrainer._id()) : opponent.monster().equals(beastInfoController2.getMonster()._id()))
                        .findFirst()
                        .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id()));
            }
            disposables.add(eventListener.listen("trainers." + this.allyTrainer._id() + ".monsters." + allyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> allyMonster = updateMonsterInfo(monsterEvent, beastInfoController2, allyMonsters)));
        } else if (enemyTrainer) {
            if (cache.getCurrentEncounter().isWild()) {
                cache.getCurrentOpponents().stream()
                        .filter(opponent -> opponent.trainer().equals(wildTrainerId))
                        .findFirst()
                        .ifPresent(opponent -> renderBeastController2.setMonsterOneOpponentId(opponent._id()));
            } else {
                cache.getCurrentOpponents().stream()
                        .filter(opponent -> opponent.monster() == null ? opponent.trainer().equals(this.enemyTrainer._id()) : opponent.monster().equals(enemyBeastInfoController1.getMonster()._id()))
                        .findFirst()
                        .ifPresent(opponent -> renderBeastController2.setMonsterOneOpponentId(opponent._id()));
            }
            disposables.add(eventListener.listen("trainers." +
                            (cache.getCurrentEncounter().isWild() ? wildTrainerId : (this.enemyTrainer == null ? this.enemyAllyTrainer._id() : this.enemyTrainer._id())) + ".monsters." + this.enemyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        enemyMonster = monsterEvent.data();
                        enemyBeastInfoController1.setLifeBarValue(enemyMonster.currentAttributes().health() / monsterEvent.data().attributes().health(), false);
                        enemyBeastInfoController1.setStatus(enemyMonster.status(), false);
                        updateMonsterInList(enemyMonster, enemyMonsters);
                    }));
        } else if (enemyAllyTrainer) {
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(enemyBeastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController2.setMonsterTwoOpponentId(opponent._id()));
            disposables.add(eventListener.listen("trainers." + this.enemyAllyTrainer._id() + ".monsters." + enemyAllyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        enemyAllyMonster = monsterEvent.data();
                        enemyBeastInfoController2.setLifeBarValue(enemyAllyMonster.currentAttributes().health() / monsterEvent.data().attributes().health(), false);
                        enemyBeastInfoController2.setStatus(enemyAllyMonster.status(), false);
                        updateMonsterInList(enemyAllyMonster, enemyAllyMonsters);
                    }));
        }
    }

    private void initializeAllyBeastInfo() {
        beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
        allyMonsterInfo = beastInfoController2.render();
        beastInfoBox.getChildren().addAll(allyMonsterInfo);
        renderBeastController1.setMonster2(allyMonster);
        ownMonstersBox.getChildren().clear();
        allyMonsterParent = renderBeastController1.render();
        ownMonstersBox.getChildren().addAll(allyMonsterParent);
        HBox.setHgrow(allyMonsterParent, Priority.ALWAYS);
    }

    private void updateMonsterInList(Monster monster, List<Monster> monsters) {
        for (int i = 0; i < monsters.size(); i++) {
            if (monster == monsters.get(i)) {
                monsters.set(i, monster);
                break;
            }
        }
    }

    private String getMonsterName(String monsterId, String opponentId) {
        if (monsterId != null) {
            if (beastInfoController1.getMonster()._id().equals(monsterId)) {
                return beastInfoController1.getName();
            } else if (allyTrainer != null && beastInfoController2.getMonster()._id().equals(monsterId)) {
                return beastInfoController2.getName();
            } else if (enemyBeastInfoController1.getMonster()._id().equals(monsterId)) {
                return enemyBeastInfoController1.getName();
            } else if (enemyAllyTrainer != null && enemyBeastInfoController2.getMonster()._id().equals(monsterId)) {
                return enemyBeastInfoController2.getName();
            }
        } else if (opponentId != null) {
            if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponentId)) {
                return beastInfoController1.getName();
            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId)) {
                return beastInfoController2.getName();
            } else if (renderBeastController2.getOpponentIdMonsterOne() != null && renderBeastController2.getOpponentIdMonsterOne().equals(opponentId)) {
                return enemyBeastInfoController1.getName();
            } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(opponentId)) {
                return enemyBeastInfoController2.getName();
            }
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
                setAttack4(cache.getAbilities().computeIfAbsent(stack.pop(), id -> presetsService.getAbility(id).blockingFirst()));
            case 3:
                setAttack3(cache.getAbilities().computeIfAbsent(stack.pop(), id -> presetsService.getAbility(id).blockingFirst()));
            case 2:
                setAttack2(cache.getAbilities().computeIfAbsent(stack.pop(), id -> presetsService.getAbility(id).blockingFirst()));
            case 1:
                setAttack1(cache.getAbilities().computeIfAbsent(stack.pop(), id -> presetsService.getAbility(id).blockingFirst()));
        }
    }

    @FXML
    public void leaveEncounter() {
        if (cache.getCurrentEncounter().isWild()) {
            disposables.add(
                    encounterOpponentsService.deleteOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getOpponentByTrainerID(cache.getTrainer()._id())._id())
                            .observeOn(FX_SCHEDULER)
                            .subscribe(o -> {
                                cache.setCurrentEncounter(null);
                                cache.getCurrentOpponents().clear();

                                IngameController controller = ingameControllerProvider.get();
                                controller.setRegion(cache.getJoinedRegion());
                                app.show(controller);
                            })
            );
        }
    }

    @FXML
    public void changeMonster() {
        hasToChooseEnemy = false;
        showChangeBeast();
    }

    public void toggleInventoryItemDetails(ItemTypeDto itemTypeDto) {
        if (Objects.equals(lastItemTypeDto, itemTypeDto)) {
            itemBox.getChildren().remove(itemDetailParent);
            lastItemTypeDto = null;
            return;
        }
        setItemDetailController(itemTypeDto, !inventoryController.isShop);
    }

    private void setItemDetailController(ItemTypeDto itemTypeDto, boolean onlyInventory) {
        lastItemTypeDto = itemTypeDto;
        ItemDetailController controller = itemDetailControllerProvider.get();
        controller.setInventoryController(inventoryController);
        controller.setEncounterController(this);
        controller.setItem(itemTypeDto);
        controller.setBooleanShop(false);
        controller.setOnlyInventory(onlyInventory);
        controller.init();
        itemBox.getChildren().remove(itemDetailParent);
        itemDetailParent = controller.render();
        itemBox.getChildren().add(0, itemDetailParent);
    }

    private void showChangeBeast() {
        ChangeBeastController controller = changeBeastControllerProvider.get();
        if (isOneVersusTwo) {
            if (!chosenTarget.equals(renderBeastController1.getOpponentIdMonsterOne()) && !chosenTarget.equals(renderBeastController1.getOpponentIdMonsterTwo())) {
                actionInfoText.appendText("Choose one of your beasts to swap!\n");
                return;
            }
        }
        if (renderBeastController1.getOpponentIdMonsterOne() != null && chosenTarget.equals(renderBeastController1.getOpponentIdMonsterOne())) {
            controller.setMonsterToSwap(myMonster);
            controller.setAllyMonster(allyMonster);
            controller.monsterInSlotOne(true);
        } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && chosenTarget.equals(renderBeastController1.getOpponentIdMonsterTwo())) {
            controller.setMonsterToSwap(allyMonster);
            controller.setAllyMonster(myMonster);
            controller.monsterInSlotOne(false);
        }
        controller.setOpponentId(chosenTarget);
        controller.setEncounterController(this);
        app.show(controller);
    }

    public void setToUpdateUIOnChange() {
        this.shouldUpdateUIOnChange = true;
    }

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
        if ((renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(chosenTarget) && myMonster.currentAttributes().health() <= 0) ||
                (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(chosenTarget) && allyMonster.currentAttributes().health() <= 0)) {
            actionInfoText.appendText("Beast is dead. Choose a new one!\n");
            return;
        }
        chosenAbility = abilityDto;

        int numberOfAttackers = (int) cache.getCurrentOpponents().stream().filter(Opponent::isAttacker).count();
        if (numberOfAttackers < 2) {
            setAttackBoxesDisabled(true);

            disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                            cache.getCurrentEncounter()._id(), chosenTarget, null,
                            new AbilityMove("ability", abilityDto.id(), (cache.getCurrentEncounter().isWild() ? wildTrainerId : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id()))))
                    .observeOn(FX_SCHEDULER)
                    .subscribe(e -> {
                                if (e.monster().equals(beastInfoController1.getMonster()._id()) && myMonster.currentAttributes().health() <= 0) {
                                    setAttackBoxesDisabled(true);

                                } else if (beastInfoController2 != null && e.monster().equals(beastInfoController2.getMonster()._id()) && allyMonster.currentAttributes().health() <= 0) {
                                    setAttackBoxesDisabled(true);
                                }
                                oldLevel = myMonster.level();
                                oldHp = myMonster.attributes().health();
                            }
                            , error -> System.err.println("Error: " + error.getMessage())));
        } else {
            hasToChooseEnemy = true;
            actionInfoText.appendText("Please choose a target!\n");
        }
    }

    public void fightIsOver() {
        EndScreenController endScreenController;

        if (myMonster.currentAttributes().health() <= 0) {
            endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
            app.show(endScreenController);
        } else {
            endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
            levelUp(myMonster, endScreenController);
        }
    }

    public void updateOurMonster(Opponent opponent) {
        boolean foundMonsterWithHP = false;

        if (opponent.monster() != null) {
            if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponent._id())) {
                myMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), myMonster._id()).blockingFirst();
                beastInfoController1.hpLabel.setText((int) myMonster.currentAttributes().health() + " / " + (int) myMonster.attributes().health() + " (HP)");
                beastInfoController1.setLifeBarValue(myMonster.currentAttributes().health() / myMonster.attributes().health(), true);
                beastInfoController1.setStatus(myMonster.status(), true);
            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponent._id())) {
                allyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), allyTrainer._id(), allyMonster._id()).blockingFirst();
                beastInfoController2.hpLabel.setText((int) allyMonster.currentAttributes().health() + " / " + (int) allyMonster.attributes().health() + " (HP)");
                beastInfoController2.setLifeBarValue(allyMonster.currentAttributes().health() / allyMonster.attributes().health(), true);
                beastInfoController2.setStatus(allyMonster.status(), true);
            }
        } else {
            for (Monster monster : ownMonsters) {
                if (!monster._id().equals(myMonster._id()) && cache.getTrainer().team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                    foundMonsterWithHP = true;
                    break;
                }
            }
            System.out.println(foundMonsterWithHP);

            if (isOneVersusTwo) {
                System.out.println("is a one versus two or two versus two");
                // Also check if my ally still has monsters
                if (foundMonsterWithHP && allyTrainer != null && allyTrainer._id().equals(cache.getTrainer()._id())) {
                    foundMonsterWithHP = false;
                    for (Monster monster : allyMonsters) {
                        if (!monster._id().equals(allyMonster._id()) && allyTrainer.team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                            foundMonsterWithHP = true;
                            break;
                        }
                    }
                    if (!foundMonsterWithHP) {
                        EndScreenController endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
                        app.show(endScreenController);
                    }
                }
            } else {
                // We lost = show lose screen
                if (!foundMonsterWithHP) {
                    EndScreenController endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
                    app.show(endScreenController);
                } else {
                    showChangeBeast();
                }
            }
        }
    }

    private void updateEnemyMonster(Opponent opponent) {
        // Check for 1v2 or 2v2 situation, where one of the attackers is gone but the other is still there (to prevent instant win after one of the attacker is down)
        if (isOneVersusTwo) {
            if (enemyMonster.currentAttributes().health() <= 0 || enemyAllyMonster.currentAttributes().health() <= 0) {
                boolean foundMonsterWithHP = false;
                for (Monster monster : enemyMonsters) {
                    if (!monster._id().equals(enemyMonster._id()) && enemyTrainer.team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                        foundMonsterWithHP = true;
                        break;
                    }
                }
                if (foundMonsterWithHP) {
                    foundMonsterWithHP = false;
                    for (Monster monster : enemyAllyMonsters) {
                        if (!monster._id().equals(enemyAllyMonster._id()) && enemyAllyTrainer.team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                            foundMonsterWithHP = true;
                            break;
                        }
                    }
                    if (!foundMonsterWithHP) {
                        EndScreenController endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
                        levelUp(myMonster, endScreenController);
                    }
                }
            }
        } else {
            if (enemyMonster.currentAttributes().health() <= 0) {
                EndScreenController endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
                levelUp(myMonster, endScreenController);
            }
        }
    }

    public void updateUIOnChange() {
        // Get the monster from the current opponents of the encounter
        disposables.add(encounterOpponentsService.getEncounterOpponents(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    if (o.isEmpty()) {
                        fightIsOver(); // set endScreen
                    } else {
                        for (Opponent opponent : o) {
                            // Check if the opponent is our trainers id
                            if (opponent.trainer().equals(cache.getTrainer()._id())) { // our monster
                                updateOurMonster(opponent);
                            } else { // enemy monster
                                if (opponent.monster() != null) {
                                    updateEnemyMonster(opponent);
                                } else {
                                    EndScreenController endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
                                    levelUp(myMonster, endScreenController);
                                }
                            }
                        }
                    }
                }));
    }

    public void setChosenTarget(String opponentId) {
        if (hasToChooseEnemy) {
            if (renderBeastController1.getOpponentIdMonsterOne() != null && opponentId.equals(renderBeastController1.getOpponentIdMonsterOne())) {
                actionInfoText.appendText("Can't choose yourself!\n");
            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && opponentId.equals(renderBeastController1.getOpponentIdMonsterTwo())) {
                actionInfoText.appendText("Can't choose your ally!\n");
            } else {
                actionInfoText.appendText("Chosen target: " + getMonsterName(null, opponentId) + "\n");

                // Set ability to use
                disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                                cache.getCurrentEncounter()._id(), chosenTarget, null,
                                new AbilityMove("ability", chosenAbility.id(), opponentId))
                        .observeOn(FX_SCHEDULER)
                        .subscribe(e -> {
                            hasToChooseEnemy = false;
                            chosenAbility = null;
                        }));
            }
        } else {
            if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponentId) && beastInfoController1.getMonster().trainer().equals(cache.getTrainer()._id())) {
                setNumberOfAttacks(beastInfoController1.getMonster());
                chosenTarget = opponentId;
            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId) && beastInfoController2.getMonster().trainer().equals(cache.getTrainer()._id())) {
                setNumberOfAttacks(beastInfoController2.getMonster());
                chosenTarget = opponentId;
            }
        }
    }

    public void levelUp(Monster myMon, EndScreenController endScreenController) {
        if (myMon.level() > oldLevel) { //Level Up
            LevelUpController controller = levelUpControllerProvider.get();
            if (myMon.abilities().size() > myMonster.abilities().size()) { //new attack
                if (myMon.type() != myMonster.type()) { // Evolved
                    controller.setBeast(myMonster, true, true, (myMonster.attributes().health() - oldHp), endScreenController);
                } else {
                    controller.setBeast(myMon, true, false, (myMonster.attributes().health() - oldHp), endScreenController);
                }
            } else {
                controller.setBeast(myMon, false, false, (myMonster.attributes().health() - oldHp), endScreenController);
            }
            app.show(controller);
        } else {
            app.show(endScreenController);
        }
    }

    public EndScreenController setEndScreen(boolean wonFight, Monster loser1, Monster loser2, Monster winner1, Monster winner2) {
        EndScreenController controller = endScreenControllerProvider.get();
        controller.setWinner(wonFight);
        if (wonFight) {
            cache.setTrainer(trainerService.getTrainer(cache.getJoinedRegion()._id(), cache.getTrainer()._id()).blockingFirst());
            float newCoinNum = cache.getTrainer().coins();
            if ((newCoinNum - oldCoinNum) != 0) {
                controller.setGainedCoins("Congratulations! You gained " + (newCoinNum - oldCoinNum) + " coins!");
            }
        }
        controller.setLoserMonster1(loser1);
        if (loser2 != null) {
            controller.setLoserMonster2(loser2);
        }
        controller.setWinnerMonster1(winner1);
        if (winner2 != null) {
            controller.setWinnerMonster2(winner2);
        }
        return controller;
    }

    private void setAttackBoxesDisabled(boolean disabled) {
        attackBox1.setDisable(disabled);
        attackBox2.setDisable(disabled);
        attackBox3.setDisable(disabled);
        attackBox4.setDisable(disabled);
    }

    public Monster getChosenMonster() {
        if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(chosenTarget)) {
            return beastInfoController1.getMonster();
        } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(chosenTarget)) {
            return beastInfoController2.getMonster();
        }
        return null;
    }

    public String getChosenTarget() {
        return chosenTarget;
    }

    @FXML
    public void showItems() {
        hasToChooseEnemy = false;
        if (inventoryController != null) {
            inventoryController.destroy();
            itemBox.getChildren().remove(inventoryParent);
        }
        InventoryController inventoryController = inventoryControllerProvider.get();
        inventoryParent = inventoryController.render();
        inventoryController.setIfShop(false);
        inventoryController.setOnItemClicked(this::toggleInventoryItemDetails);
        inventoryController.setOnCloseRequest(() -> {
            itemBox.getChildren().remove(itemDetailParent);
            itemBox.getChildren().remove(inventoryParent);
            anchorPane.toBack();
            anchorPane.setStyle("-fx-background-color: none;");
        });
        itemBox.getChildren().add(inventoryParent);
        anchorPane.toFront();
        anchorPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
    }

    public void setCatchInfoBox(boolean caught) {
        CatchInfoController catchInfoController = catchInfoControllerProvider.get();
        if (caught) {
            String catchInfo = resources.getString("successCatch");
            String teamInfo = "";
            if (cache.getTrainer().team().size() < 6) {
                teamInfo = resources.getString("catchToTeam");
                List<String> newTeam = cache.getTrainer().team();
                newTeam.add(enemyMonster._id());
                disposables.add(trainerService.updateTrainer(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), null, null, newTeam).observeOn(FX_SCHEDULER).subscribe(
                        trainer -> cache.setTrainer(trainer)
                ));
            }
            catchInfoController = catchInfoController.setCatchInfo(catchInfo, teamInfo, enemyMonster.type());
            Parent catchInfoParent = catchInfoController.render();
            catchInfoController.setOnCloseRequest(() -> {
                catchInfoBox.getChildren().remove(catchInfoParent);
                infoAnchorPane.toBack();
                infoAnchorPane.setStyle("-fx-background-color: none;");
                IngameController controller = ingameControllerProvider.get();
                controller.setRegion(cache.getJoinedRegion());
                app.show(controller);
            });
            catchInfoBox.getChildren().add(catchInfoParent);
            infoAnchorPane.toFront();
            infoAnchorPane.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
            showItemAnimation(renderBeastController2, null, -1);
        } else {
            System.out.println("Catch failed");
            showItemAnimation(renderBeastController2, null, -5);
        }

    }

    public void setMonBallUsed(boolean used) {
        monBallUsed = used;
    }

    public void showItemAnimation(RenderBeastController renderBeastController, VBox selectBox, int itemId) {
        if (itemId == -5) {
            Image catchFailedImage = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/close.png")));
            renderBeastController.setItemAnimation(catchFailedImage, selectBox);
        } else {
            int usedItemId = itemId == -1 ? usedItemTypeDto.id() : itemId;
            if (cache.getItemImages().containsKey(usedItemId)) {
                renderBeastController.setItemAnimation(cache.getItemImages().get(usedItemId), selectBox);
            } else {
                disposables.add(presetsService.getItemImage(usedItemId)
                        .observeOn(FX_SCHEDULER)
                        .subscribe(itemImage -> {
                            Map<Integer, Image> itemImages = new HashMap<>();
                            itemImages.put(usedItemId, itemImage);
                            cache.setItemImages(itemImages);
                            renderBeastController.setItemAnimation(cache.getItemImages().get(usedItemId), selectBox);
                        }));
            }
        }
    }
}