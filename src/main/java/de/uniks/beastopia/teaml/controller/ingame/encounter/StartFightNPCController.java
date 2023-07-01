package de.uniks.beastopia.teaml.controller.ingame.encounter;


import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
                                        String message = (prefs.getLocale().contains("de")) ? " startet einen Kampf " +
                                                "gegen dich!" : " starts a fight against you!";
                                        headline.setText(t.name() + message);
                                        image.setImage(cache.getCharacterImage(t.image()).getValue());
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
