package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ChangeMonsterMove;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class ChangeBeastController extends Controller {
    private final List<Monster> fightingMonsters = new ArrayList<>();
    private final List<Monster> bankMonsters = new ArrayList<>();
    private final List<ChangeBeastElementController> subControllers = new ArrayList<>();
    @FXML
    public GridPane yourBeastsPane;
    @FXML
    public GridPane fightingBeastPane;
    @FXML
    public VBox currentBeasts;
    @FXML
    public VBox beastTeam;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    TrainerService trainerService;
    @Inject
    DataCache cache;
    @Inject
    Provider<ChangeBeastElementController> changeBeastElementControllerProvider;
    private Monster monsterToSwap;
    private Monster allyMonster;
    private String opponentId;
    private EncounterController encounterController;
    private LoadingPage loadingPage;
    private boolean hasToSwapMonsterOne;

    @Inject
    public ChangeBeastController() {
    }

    public void setMonsterToSwap(Monster currentMonster) {
        this.monsterToSwap = currentMonster;
    }

    public void setAllyMonster(Monster allyMonster) {
        this.allyMonster = allyMonster;
    }

    public void setOpponentId(String opponent) {
        this.opponentId = opponent;
    }

    public void setEncounterController(EncounterController controller) {
        this.encounterController = controller;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleChangeBeasts");
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        disposables.add(trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    for (Monster teamMonster : monsters) {
                        if (cache.getTrainer().team().contains(teamMonster._id())) {
                            ChangeBeastElementController controller = changeBeastElementControllerProvider.get()
                                    .setMonster(teamMonster)
                                    .setParentController(this);

                            subControllers.add(controller);

                            if (teamMonster._id().equals(monsterToSwap._id())) {
                                //monsterToSwap = teamMonster;

                                fightingMonsters.add(teamMonster);
                                currentBeasts.getChildren().add(controller.render());
                            } else {
                                bankMonsters.add(teamMonster);
                                beastTeam.getChildren().add(controller.render());
                            }
                        }
                    }

                    loadingPage.setDone();
                }));

        return loadingPage.parent();
    }

    @FXML
    public void back() {
        app.showPrevious();
    }

    @FXML
    public void changeBeast() {
        if (fightingMonsters.isEmpty()) {
            Dialog.error(resources.getString("error"), resources.getString("noMonSelected"));
        } else if (fightingMonsters.size() > 1) {
            Dialog.error(resources.getString("error"), resources.getString("twoMonSelected"));
        } else if (fightingMonsters.get(0).currentAttributes().health() == 0) {
            Dialog.error(resources.getString("error"), resources.getString("monNoHPLeft"));
        } else if (monsterToSwap._id().equals(fightingMonsters.get(0)._id())) {
            Dialog.error(resources.getString("error"), resources.getString("currentMonIsSame"));
        } else if (allyMonster != null && fightingMonsters.get(0)._id().equals(allyMonster._id())) {
            Dialog.error(resources.getString("error"), resources.getString("currentMonAlreadyFighting"));
        } else {
            disposables.add(encounterOpponentsService.getTrainerOpponents(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                    .subscribe(opponents -> {
                        // Switch monster without expending a move. Otherwise, make a move
                        for (Opponent opponent : opponents) {
                            if (opponent._id().equals(opponentId)) {
                                if (opponent.monster() == null) {
                                    disposables.add(encounterOpponentsService.updateEncounterOpponent(
                                                    cache.getJoinedRegion()._id(),
                                                    cache.getCurrentEncounter()._id(),
                                                    opponent._id(),
                                                    fightingMonsters.get(0)._id()
                                            )
                                            .observeOn(FX_SCHEDULER)
                                            .subscribe(update -> {
                                                cache.updateCurrentOpponents(update);
                                                setChangedMonster();
                                                //encounterController.setToUpdateUIOnChange();
                                                app.show(encounterController);
                                            }));
                                } else {
                                    disposables.add(encounterOpponentsService.updateEncounterOpponent(
                                                    cache.getJoinedRegion()._id(),
                                                    cache.getCurrentEncounter()._id(),
                                                    opponent._id(),
                                                    null,
                                                    new ChangeMonsterMove("change-monster", fightingMonsters.get(0)._id())
                                            )
                                            .observeOn(FX_SCHEDULER)
                                            .subscribe(update -> {
                                                cache.updateCurrentOpponents(update);
                                                setChangedMonster();
                                                encounterController.setToUpdateUIOnChange();
                                                app.show(encounterController);
                                            }));
                                }
                            }
                        }
                    }));
        }
    }

    public List<Monster> getFightingMonsters() {
        return this.fightingMonsters;
    }

    public List<Monster> getBankMonsters() {
        return this.bankMonsters;
    }

    public void monsterInSlotOne(boolean value) {
        this.hasToSwapMonsterOne = value;
    }

    private void setChangedMonster() {
        if (hasToSwapMonsterOne) {
            encounterController.setOwnMonster(fightingMonsters.get(0));
        } else {
            encounterController.setAllyMonster(fightingMonsters.get(0));
        }
    }

    @Override
    public void destroy() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
        beastTeam.getChildren().clear();
        fightingBeastPane.getChildren().clear();
        super.destroy();
    }
}
