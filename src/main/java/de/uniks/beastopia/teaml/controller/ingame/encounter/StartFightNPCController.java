package de.uniks.beastopia.teaml.controller.ingame.encounter;


import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Encounter;
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


import static de.uniks.beastopia.teaml.controller.ingame.TrainerController.PREVIEW_VIEWPORT;
import static de.uniks.beastopia.teaml.service.PresetsService.PREVIEW_SCALING;

public class StartFightNPCController extends Controller {
    @FXML
    public Label headline;
    @FXML
    public ImageView image;
    private Encounter encounter;
    @Inject
    Prefs prefs;
    @Inject
    TrainerService trainerService;
    @Inject
    DataCache cache;
    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    PresetsService presetsService;
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
        //TODO start EncounterScreen
    }
}
