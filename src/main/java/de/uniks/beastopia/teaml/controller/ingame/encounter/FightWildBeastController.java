package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class FightWildBeastController extends Controller {

    @FXML
    public Label headline;
    @FXML
    public ImageView image;
    @FXML
    public Button startFight;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    Prefs prefs;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    EncounterController encounterController;
    @Inject
    Provider<EncounterController> encounterControllerProvider;
    @Inject
    DataCache cache;
    private String beastId;
    private String trainerId;
    private int type;

    @Inject
    public FightWildBeastController() {

    }

    public void setControllerInfo(String beastId, String trainerId) {
        this.beastId = beastId;
        this.trainerId = trainerId;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(trainerService.getTrainerMonster(prefs.getRegionID(), trainerId, beastId)
                .observeOn(FX_SCHEDULER)
                .concatMap(b -> { //Nacheinander ausführen
                    this.type = b.type();
                    return presetsService.getMonsterType(this.type);
                }).observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    if (prefs.getLocale().contains("de")) {
                        headline.setText("Ein wildes " + type.name() + " erscheint!");
                    } else {
                        headline.setText("A wild " + type.name() + " appears!");
                    }
                }));

        disposables.add(trainerService.getTrainerMonster(prefs.getRegionID(), trainerId, beastId)
                .observeOn(FX_SCHEDULER)
                .concatMap(b -> { //Nacheinander ausführen
                    this.type = b.type();
                    return presetsService.getMonsterImage(this.type);
                }).observeOn(FX_SCHEDULER).subscribe(beastImage -> image.setImage(beastImage)));

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
                    Trainer enemyAllyTrainer = null;
                    Trainer allyTrainer = null;
                    Trainer enemyTrainer = null;

                    List<Monster> myMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                    List<Monster> enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                    Monster ownMonster = myMonsters.stream().filter(m -> m._id().equals(o.get(0).monster())).findFirst().orElseThrow();
                    Monster enemyMonster = enemyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow();

                    if (o.size() == 3) {
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(2).trainer()).blockingFirst();
                        enemyAllyMonster = enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(2).monster())).findFirst().orElseThrow();
                        enemyAllyTrainer = trainerService.getTrainer(cache.getJoinedRegion()._id(), o.get(2).trainer()).blockingFirst();
                    } else if (o.size() == 4) {
                        List<Monster> myAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(3).trainer()).blockingFirst();
                        allyMonster = myAllyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow();
                        enemyAllyMonster = enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(3).monster())).findFirst().orElseThrow();
                        allyTrainer = trainerService.getTrainer(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        enemyTrainer = trainerService.getTrainer(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                        enemyAllyTrainer = trainerService.getTrainer(cache.getJoinedRegion()._id(), o.get(3).trainer()).blockingFirst();
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
