package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
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
                .observeOn(FX_SCHEDULER)
                .subscribe(o -> {
                    disposables.add(encounterOpponentsService
                            .deleteOpponent(cache.getJoinedRegion()._id(), o.get(0).encounter(), o.get(0)._id())
                            .observeOn(FX_SCHEDULER).subscribe(e -> {
                            }));
                }));

    }
    //TODO show Encounter screen, das andere hierrueber nur zum Loeschen (Uebergangsloesung)
}
