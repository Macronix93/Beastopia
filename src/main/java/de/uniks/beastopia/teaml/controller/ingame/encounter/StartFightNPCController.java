package de.uniks.beastopia.teaml.controller.ingame.encounter;


import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

import static de.uniks.beastopia.teaml.controller.ingame.TrainerController.PREVIEW_VIEWPORT;
import static de.uniks.beastopia.teaml.service.PresetsService.PREVIEW_SCALING;

public class StartFightNPCController extends Controller {
    @FXML
    public Label headline;
    @FXML
    public ImageView image;
    private Encounter encounter;
    @Inject
    TrainerService trainerService;
    @Inject
    DataCache cache;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    EncounterController encounterController;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<EncounterController> encounterControllerProvider;

    @Inject
    public StartFightNPCController() {

    }

    public void setControllerInfo(Encounter encounter) {
        this.encounter = encounter;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(encounterOpponentsService.getEncounterOpponents(cache.getJoinedRegion()._id(), encounter._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(ops -> {
                    for (Opponent o : ops) {
                        if (!(o.trainer().equals(cache.getTrainer()._id()))) {
                            disposables.add(trainerService.getTrainer(cache.getJoinedRegion()._id(), o.trainer())
                                    .observeOn(FX_SCHEDULER)
                                    .subscribe(t -> {
                                        headline.setText(t.name() + " " + resources.getString("npcStart"));
                                        disposables.add(presetsService
                                                .getCharacterSprites(t.image(), true)
                                                .observeOn(FX_SCHEDULER)
                                                .subscribe(i -> {
                                                    image.setImage(i);
                                                    image.setViewport(PREVIEW_VIEWPORT);
                                                    image.setFitWidth(32 * PREVIEW_SCALING);
                                                    image.setFitHeight(64 * PREVIEW_SCALING);
                                                }));
                                    }));
                            return;
                        }
                    }
                }));

        return parent;
    }

    @FXML
    public void startFight() {
        disposables.add(encounterOpponentsService.getEncounterOpponents(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    cache.setCurrentOpponents(o);

                    Monster allyMonster = null;
                    Monster enemyAllyMonster = null;
                    String enemyAllyTrainer = null;
                    String allyTrainer = null;

                    String enemyTrainer = o.get(0).trainer();
                    List<Monster> myMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                    List<Monster> enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                    Monster ownMonster = myMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow();
                    Monster enemyMonster = enemyMonsters.stream().filter(m -> m._id().equals(o.get(0).monster())).findFirst().orElseThrow();

                    if (o.size() == 3) {
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(2).trainer()).blockingFirst();
                        enemyAllyMonster = enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(2).monster())).findFirst().orElseThrow();
                        enemyAllyTrainer = o.get(2).trainer();
                    } else if (o.size() == 4) {
                        List<Monster> myAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(3).trainer()).blockingFirst();
                        allyMonster = myAllyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow();
                        enemyAllyMonster = enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(3).monster())).findFirst().orElseThrow();
                        allyTrainer = o.get(1).trainer();
                        enemyTrainer = o.get(0).trainer();
                        enemyAllyTrainer = o.get(3).trainer();
                    }

                    EncounterController controller = encounterControllerProvider.get()
                            .setOwnMonster(ownMonster)
                            .setEnemyMonster(enemyMonster)
                            .setOwnMonsters(myMonsters)
                            .setEnemyMonsters(enemyMonsters)
                            .setAllyMonster(allyMonster)
                            .setEnemyAllyMonster(enemyAllyMonster)
                            .setEnemyTrainer(enemyTrainer)
                            .setAllyTrainer(allyTrainer)
                            .setEnemyAllyTrainer(enemyAllyTrainer);

                    app.show(controller);
                }));

    }
}
