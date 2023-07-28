package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ChangeMonsterMove;
import de.uniks.beastopia.teaml.rest.Monster;
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
    @Inject
    Provider<EncounterController> encounterControllerProvider;

    private final List<Monster> fightingMonsters = new ArrayList<>();
    private final List<Monster> bankMonsters = new ArrayList<>();
    private final List<ChangeBeastElementController> subControllers = new ArrayList<>();
    private Monster currentMonster;
    private EncounterController encounterController;
    private LoadingPage loadingPage;

    @Inject
    public ChangeBeastController() {
    }

    public void setCurrentMonster(Monster currentMonster) {
        this.currentMonster = currentMonster;
    }

    public void setEncounterController(EncounterController controller) {
        this.encounterController = controller;
    }

    @Override
    public void init() {
        super.init();
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

                            if (teamMonster._id().equals(currentMonster._id())) {
                                currentMonster = teamMonster;

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
        } else if (currentMonster._id().equals(fightingMonsters.get(0)._id())) {
            Dialog.error(resources.getString("error"), resources.getString("currentMonIsSame"));
        } else {
            disposables.add(encounterOpponentsService.getTrainerOpponents(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                    .subscribe(opponents -> {
                        String opponentId = cache.getOpponentByTrainerID(cache.getTrainer()._id())._id();
                        String monsterId = (opponents.get(0).monster() == null) ? fightingMonsters.get(0)._id() : null;

                        // Switch monster without expending a move. Otherwise, make a move
                        if (opponents.get(0).monster() == null) {
                            disposables.add(encounterOpponentsService.updateEncounterOpponent(
                                            cache.getJoinedRegion()._id(),
                                            cache.getCurrentEncounter()._id(),
                                            opponentId,
                                            fightingMonsters.get(0)._id()
                                    )
                                    .observeOn(FX_SCHEDULER)
                                    .subscribe(update -> {
                                        encounterController.setOwnMonster(fightingMonsters.get(0));
                                        encounterController.setToUpdateUIOnChange();
                                        app.show(encounterController);
                                    }));
                        } else {
                            disposables.add(encounterOpponentsService.updateEncounterOpponent(
                                            cache.getJoinedRegion()._id(),
                                            cache.getCurrentEncounter()._id(),
                                            opponentId,
                                            monsterId,
                                            new ChangeMonsterMove("change-monster", fightingMonsters.get(0)._id())
                                    )
                                    .observeOn(FX_SCHEDULER)
                                    .subscribe(update -> {
                                        encounterController.setOwnMonster(fightingMonsters.get(0));
                                        encounterController.setToUpdateUIOnChange();
                                        app.show(encounterController);
                                    }));
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
