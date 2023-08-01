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
    Provider<LevelUpController> levelUpControllerProvider;
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
    private int oldLevel;
    private int oldHp;
    //monsters in the fight
    Monster myMonster;
    Monster allyMonster;
    Monster enemyMonster;
    Monster enemyAllyMonster;

    Trainer myTrainer;
    @SuppressWarnings({"FieldCanBeLocal"})
    Trainer allyTrainer;
    Trainer enemyTrainer;
    @SuppressWarnings({"FieldCanBeLocal"})
    Trainer enemyAllyTrainer;

    private boolean shouldUpdateUIOnChange = false;
    private String chosenTarget = null;
    private boolean hasToChooseEnemy = false;
    private AbilityDto chosenAbility;
    Parent myMonsterParent;
    Parent allyMonsterParent;
    Parent enemyMonsterParent;
    Parent enemyAllyMonsterParent;
    Parent myMonsterInfo;
    Parent allyMonsterInfo;
    Parent enemyMonsterInfo;
    Parent enemyAllyMonsterInfo;

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

        oldCoinNum = cache.getTrainer().coins();

        if (myTrainer != null) {
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
                    .filter(opponent -> opponent.monster() == null ? opponent.trainer().equals(myTrainer._id()) : opponent.monster().equals(beastInfoController1.getMonster()._id()))
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
                        beastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false);
                        if (monsterEvent.data().currentAttributes().health() >= 0) {
                            myMonster = monsterEvent.data();
                        }
                    }));
        }

        if (allyTrainer != null) {
            beastInfoController2 = beastInfoControllerProvider.get().setMonster(allyMonster);
            allyMonsterInfo = beastInfoController2.render();
            beastInfoBox.getChildren().addAll(allyMonsterInfo);
            renderBeastController1.setMonster2(allyMonster);
            ownMonstersBox.getChildren().clear();
            allyMonsterParent = renderBeastController1.render();
            ownMonstersBox.getChildren().addAll(allyMonsterParent);
            HBox.setHgrow(allyMonsterParent, Priority.ALWAYS);

            // Set ally monster opponent ID
            if (allyTrainer._id().equals(myTrainer._id())) {
                // 2v2 situation
                if (renderBeastController1.getOpponentIdMonsterOne() != null) {
                    for (Opponent o : cache.getCurrentOpponents()) {
                        if (o.trainer().equals(allyTrainer._id()) && !o._id().equals(renderBeastController1.getOpponentIdMonsterOne())) {
                            renderBeastController1.setMonsterTwoOpponentId(o._id());
                            break;
                        }
                    }
                }
            } else {
                cache.getCurrentOpponents().stream()
                        .filter(opponent -> opponent.monster() == null ? opponent.trainer().equals(allyTrainer._id()) : opponent.monster().equals(beastInfoController2.getMonster()._id()))
                        .findFirst()
                        .ifPresent(opponent -> renderBeastController1.setMonsterTwoOpponentId(opponent._id())
                        );
            }

            // Add monster listener for ally
            addAllyListener();
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

            // Set enemy monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> {
                        if (cache.getCurrentEncounter().isWild() && opponent.isNPC()) {
                            return opponent.trainer().equals("000000000000000000000000");
                        } else {
                            return opponent.monster() == null ? opponent.trainer().equals(enemyTrainer._id()) : opponent.monster().equals(enemyBeastInfoController1.getMonster()._id());
                        }
                    })
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController2.setMonsterOneOpponentId(opponent._id()));

            // Add monster listener for enemy
            disposables.add(eventListener.listen("trainers." + (cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id())) + ".monsters." + this.enemyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                        enemyBeastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false);
                        if (monsterEvent.data().currentAttributes().health() >= 0) {
                            enemyMonster = monsterEvent.data();
                        }
                    }));
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

            // Set enemy ally monster opponent ID
            cache.getCurrentOpponents().stream()
                    .filter(opponent -> opponent.monster().equals(enemyBeastInfoController2.getMonster()._id()))
                    .findFirst()
                    .ifPresent(opponent -> renderBeastController2.setMonsterTwoOpponentId(opponent._id()));

            // Add monster listener for enemy ally
            disposables.add(eventListener.listen("trainers." + enemyAllyTrainer._id() + ".monsters." + enemyAllyMonster._id() + ".updated", Monster.class)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterEvent -> {
                                enemyBeastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false);
                                if (monsterEvent.data().currentAttributes().health() >= 0) {
                                    enemyAllyMonster = monsterEvent.data();
                                }
                            }
                    ));
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
                                addAllyListener();
                            } else if (o.suffix().equals("updated")) {
                                if (o.data() != null) {
                                    String opponentId = o.data()._id();
                                    List<Result> results = o.data().results();
                                    String prefix = o.data().trainer().equals(cache.getTrainer()._id()) ? "My " : "Enemy ";

                                    if (renderBeastController2.getOpponentIdMonsterOne().equals(opponentId)) {
                                        // Update enemy monster in slot one, if it's not the same anymore
                                        if (!enemyBeastInfoController1.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                Monster newEnemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
                                                enemyBeastInfoController1.setMonster(newEnemyMonster);
                                                enemyBeastInfoController1.setLevel(newEnemyMonster.level());
                                                enemyBeastInfoController1.setLifeBarValue(newEnemyMonster.currentAttributes().health() / (double) newEnemyMonster.attributes().health(), false);
                                                disposables.add(presetsService.getMonsterType(newEnemyMonster.type())
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterType -> {
                                                            enemyBeastInfoController1.setName(monsterType.name());
                                                            renderBeastController2.setImageMonsterOne(presetsService.getMonsterImage(monsterType.id()).blockingFirst());

                                                            actionInfoText.appendText(enemyTrainer.name() + " sent out new beast " + monsterType.name() + ".\n");
                                                        }));
                                                renderBeastController2.setMonsterOneOpponentId(opponentId);
                                                renderBeastController2.setMonster1(newEnemyMonster);
                                                enemyMonster = newEnemyMonster;

                                                disposables.add(eventListener.listen("trainers." + enemyTrainer._id() + ".monsters." + newEnemyMonster._id() + ".updated", Monster.class)
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterEvent -> enemyBeastInfoController1.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false)));
                                            }
                                        }
                                    } else if (renderBeastController2.getOpponentIdMonsterTwo() != null && renderBeastController2.getOpponentIdMonsterTwo().equals(opponentId)) {
                                        // Update enemy monster in slot two, if it's not the same anymore
                                        if (!enemyBeastInfoController2.getMonster()._id().equals(o.data().monster())) {
                                            // Get the updated monster and set new values
                                            if (o.data().monster() != null) {
                                                Monster newEnemyAllyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), o.data().trainer(), o.data().monster()).blockingFirst();
                                                enemyBeastInfoController2.setMonster(newEnemyAllyMonster);
                                                enemyBeastInfoController2.setLevel(newEnemyAllyMonster.level());
                                                enemyBeastInfoController2.setLifeBarValue(newEnemyAllyMonster.currentAttributes().health() / (double) newEnemyAllyMonster.attributes().health(), false);
                                                disposables.add(presetsService.getMonsterType(newEnemyAllyMonster.type())
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterType -> {
                                                            enemyBeastInfoController2.setName(monsterType.name());
                                                            renderBeastController2.setImageMonsterTwo(presetsService.getMonsterImage(monsterType.id()).blockingFirst());

                                                            actionInfoText.appendText(enemyAllyTrainer.name() + " sent out new beast " + monsterType.name() + ".\n");
                                                        }));
                                                renderBeastController2.setMonsterTwoOpponentId(opponentId);
                                                renderBeastController2.setMonster2(newEnemyAllyMonster);
                                                enemyAllyMonster = newEnemyAllyMonster;

                                                disposables.add(eventListener.listen("trainers." + enemyAllyTrainer._id() + ".monsters." + newEnemyAllyMonster._id() + ".updated", Monster.class)
                                                        .observeOn(FX_SCHEDULER)
                                                        .subscribe(monsterEvent -> {
                                                                    enemyBeastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false);
                                                                    if (monsterEvent.data().currentAttributes().health() >= 0) {
                                                                        enemyAllyMonster = monsterEvent.data();
                                                                    }
                                                                }
                                                        ));
                                            }
                                        }
                                    }

                                    // Update opponent in cache and show battle results
                                    cache.updateCurrentOpponents(o.data());
                                    String monsterName = getMonsterName(o.data().monster(), null);
                                    if (monsterName != null && results != null) {
                                        if (o.data().trainer().equals(cache.getTrainer()._id())) {
                                            System.out.println("enable attack boxes");
                                            setAttackBoxesDisabled(false);
                                        }

                                        for (Result result : results) {
                                            switch (result.type()) {
                                                case "ability-success" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". It was " + result.effectiveness() + ".\n");
                                                case "ability-failed" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". It failed due to status!\n");
                                                case "ability-no-uses" ->
                                                        actionInfoText.appendText(prefix + monsterName + " used " + result.ability() + ". There are no ability points left!\n");
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
                                            }
                                            if (result.status() != null && !result.type().equals("status-removed") && !result.type().equals("status-damage")) {
                                                actionInfoText.appendText(prefix + getMonsterName(result.monster(), null) + " is " + result.status() + "!\n");
                                            }
                                        }
                                    }
                                }
                            } else if (o.suffix().equals("deleted")) {
                                cache.removeOpponent(o.data()._id());
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
                        },
                        error -> System.err.println("Fehler: " + error.getMessage()))
        );

        return parent;
    }

    private void addAllyListener() {
        disposables.add(eventListener.listen("trainers." + allyTrainer._id() + ".monsters." + allyMonster._id() + ".updated", Monster.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterEvent -> {
                    beastInfoController2.hpLabel.setText(monsterEvent.data().currentAttributes().health() + " / " + monsterEvent.data().attributes().health() + " (HP)");
                    beastInfoController2.setLifeBarValue(monsterEvent.data().currentAttributes().health() / (double) monsterEvent.data().attributes().health(), false);
                    if (monsterEvent.data().currentAttributes().health() >= 0) {
                        allyMonster = monsterEvent.data();
                    }
                }));
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
        hasToChooseEnemy = false;
        // TODO: Change monster of currently selected opponent (only my opponents -> 2v2 situation)
        ChangeBeastController controller = changeBeastControllerProvider.get();
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
        if ((renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(chosenTarget) && beastInfoController1.getMonster().currentAttributes().health() <= 0) ||
                (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(chosenTarget) && beastInfoController2.getMonster().currentAttributes().health() <= 0)) {
            System.out.println("mon has no hp! change please");
            return;
        }

        chosenAbility = abilityDto;

        int numberOfAttackers = (int) cache.getCurrentOpponents().stream().filter(Opponent::isAttacker).count();
        if (numberOfAttackers < 2) {
            setAttackBoxesDisabled(true);

            System.out.println(abilityDto.toString());
            System.out.println(cache.getOpponentByTrainerID(cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id())).toString());
            Monster before = myMonster;
            Monster beforeEnemy = enemyMonster;

            disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(),
                            cache.getCurrentEncounter()._id(), chosenTarget, null,
                            new AbilityMove("ability", abilityDto.id(), (cache.getCurrentEncounter().isWild() ? "000000000000000000000000" : (enemyTrainer == null ? enemyAllyTrainer._id() : enemyTrainer._id()))))
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
                    ));
        } else {
            hasToChooseEnemy = true;
            actionInfoText.appendText("Please choose a target!\n");
        }
    }

    public void fightIsOver() {
        System.out.println("o size is zero");

        Monster myMon = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), myMonster._id()).blockingFirst();
        EndScreenController endScreenController;

        if (myMon.currentAttributes().health() <= 0) {
            beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
            beastInfoController1.setLifeBarValue(0, true);

            endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
            app.show(endScreenController);
        } else {
            System.out.println("mon has > 0 hp");

            endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);

            levelUp(myMon, endScreenController);
        }
    }

    public void updateOurMonster(Opponent opponent) {
        boolean foundMonsterWithHP = false;

        if (opponent.monster() != null) {
            //myMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), opponent.monster()).blockingFirst();
            beastInfoController1.hpLabel.setText(myMonster.currentAttributes().health() + " / " + myMonster.attributes().health() + " (HP)");
            beastInfoController1.setLifeBarValue(myMonster.currentAttributes().health() / (float) myMonster.attributes().health(), false);
        } else {
            beastInfoController1.hpLabel.setText("0 / " + myMonster.attributes().health() + " (HP)");
            beastInfoController1.setLifeBarValue(0, true);
            ownMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), cache.getTrainer()._id()).blockingFirst();
            // If the monster has died during change, show 0 HP, otherwise the current HP of the monster
            for (Monster monster : ownMonsters) {
                if (!monster._id().equals(myMonster._id()) && cache.getTrainer().team().contains(monster._id()) && monster.currentAttributes().health() > 0) {
                    foundMonsterWithHP = true;
                    break;
                }
            }
            // We lost = show lose screen
            if (!foundMonsterWithHP) {
                EndScreenController endScreenController;
                endScreenController = setEndScreen(false, myMonster, allyMonster, enemyMonster, enemyAllyMonster);
                app.show(endScreenController);
            } else {
                ChangeBeastController controller = changeBeastControllerProvider.get();
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
        }
    }

    private void updateEnemyMonster(Opponent opponent) {
        System.out.println("monster is not null, update enemy hp");
        Monster beforeMonster = enemyMonster;
        //enemyMonster = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), enemyTrainer._id(), opponent.monster()).blockingFirst();

        if (enemyMonster.currentAttributes().health() <= 0) {
            System.out.println("enemy monster has zero hp");
            System.out.println("oldlevel is " + oldLevel);
            EndScreenController endScreenController;
            Monster myMon = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), myMonster._id()).blockingFirst();
            endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
            levelUp(myMon, endScreenController);
        } /*else {
            if (beforeMonster.type() != enemyMonster.type()) {
                enemyMonstersBox.getChildren().removeAll();
                renderBeastController2.destroy();
                if (renderBeastController2.monster1 == beforeMonster) {
                    renderBeastController2 = renderBeastControllerProvider.get().setMonster1(enemyMonster);
                } else {
                    renderBeastController2 = renderBeastControllerProvider.get().setMonster2(enemyMonster);
                }
                Parent enemyMonster = renderBeastController2.render();
                enemyMonstersBox.getChildren().addAll(enemyMonster);
                HBox.setHgrow(enemyMonster, Priority.ALWAYS);
            }
            //enemyBeastInfoController1.setLifeBarValue(enemyMonster.currentAttributes().health() / (double) enemyMonster.attributes().health(), false);
        }*/
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
                                System.out.println("check if enemy opponents monster is not null");
                                if (opponent.monster() != null) {
                                    updateEnemyMonster(opponent);
                                } else {
                                    System.out.println("monster is null, we won?");
                                    EndScreenController endScreenController;
                                    Monster myMon = trainerService.getTrainerMonster(cache.getJoinedRegion()._id(), cache.getTrainer()._id(), myMonster._id()).blockingFirst();
                                    endScreenController = setEndScreen(true, enemyMonster, enemyAllyMonster, myMonster, allyMonster);
                                    levelUp(myMon, endScreenController);
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
                        .subscribe(
                                e -> {
                                    hasToChooseEnemy = false;
                                    chosenAbility = null;
                                }
                        ));
            }
        } else {
            //if (chosenTarget != null && !chosenTarget.equals(opponentId)) {
            if (renderBeastController1.getOpponentIdMonsterOne() != null && renderBeastController1.getOpponentIdMonsterOne().equals(opponentId) && beastInfoController1.getMonster().trainer().equals(cache.getTrainer()._id())) {
                setNumberOfAttacks(beastInfoController1.getMonster());
                chosenTarget = opponentId;
                //this.chosenMonster = beastInfoController1.getMonster()._id();
            } else if (renderBeastController1.getOpponentIdMonsterTwo() != null && renderBeastController1.getOpponentIdMonsterTwo().equals(opponentId) && beastInfoController2.getMonster().trainer().equals(cache.getTrainer()._id())) {
                setNumberOfAttacks(beastInfoController2.getMonster());
                chosenTarget = opponentId;
                //this.chosenMonster = beastInfoController2.getMonster()._id();
            }
            //}
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
}