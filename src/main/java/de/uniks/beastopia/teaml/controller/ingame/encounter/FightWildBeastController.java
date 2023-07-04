package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
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
import javafx.scene.image.ImageView;

import javax.inject.Inject;
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
        disposables.add(encounterOpponentsService.getTrainerOpponents(cache.getJoinedRegion()._id(),
                        cache.getTrainer()._id())
                .map(o -> {
                    if (o.size() == 2) {
                        List<Monster> myMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                        List<Monster> enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        encounterController.setOwnMonster(myMonsters.stream().filter(m -> m._id().equals(o.get(0).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyMonster(enemyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyTrainer(cache.getTrainer(o.get(1).trainer()));
                    } else if (o.size() == 3) {
                        List<Monster> myMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                        List<Monster> enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(2).trainer()).blockingFirst();
                        encounterController.setOwnMonster(myMonsters.stream().filter(m -> m._id().equals(o.get(0).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyMonster(enemyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyAllyMonster(enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(2).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyTrainer(cache.getTrainer(o.get(1).trainer()));
                        encounterController.setEnemyAllyTrainer(cache.getTrainer(o.get(2).trainer()));
                    } else if (o.size() == 4) {
                        List<Monster> myMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(0).trainer()).blockingFirst();
                        List<Monster> myAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(1).trainer()).blockingFirst();
                        List<Monster> enemyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(2).trainer()).blockingFirst();
                        List<Monster> enemyAllyMonsters = trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), o.get(3).trainer()).blockingFirst();
                        encounterController.setOwnMonster(myMonsters.stream().filter(m -> m._id().equals(o.get(0).monster())).findFirst().orElseThrow());
                        encounterController.setAllyMonster(myAllyMonsters.stream().filter(m -> m._id().equals(o.get(1).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyMonster(enemyMonsters.stream().filter(m -> m._id().equals(o.get(2).monster())).findFirst().orElseThrow());
                        encounterController.setEnemyAllyMonster(enemyAllyMonsters.stream().filter(m -> m._id().equals(o.get(3).monster())).findFirst().orElseThrow());
                        encounterController.setAllyTrainer(cache.getTrainer(o.get(1).trainer()));
                        encounterController.setEnemyTrainer(cache.getTrainer(o.get(2).trainer()));
                        encounterController.setEnemyAllyTrainer(cache.getTrainer(o.get(3).trainer()));
                    }
                    return o;
                })
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    encounterController.init();
                    app.show(encounterController);
                }));

    }
}
