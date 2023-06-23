package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

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

    private String beastId;
    private String trainerId;

    private MonsterTypeDto beast;

    private Monster beeeast;

    @Inject
    public FightWildBeastController() {

    }

    public void setWildBeast(String beastId, String trainerId) {
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
                .subscribe(b -> {
                    beeeast = b;
                }));

        disposables.add(presetsService.getMonsterType(beeeast.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    if (prefs.getLocale().contains("de")) {
                        headline.setText("Ein wildes " + type.name() + " erscheint!");
                    } else {
                        headline.setText("A wild " + type.name() + " appears!");
                    }
                }));

        disposables.add(presetsService.getMonsterImage(beeeast.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(beastImage -> image.setImage(beastImage)));

        if (prefs.getLocale().contains("de")) {
            headline.setText("Ein wildes " + beast.name() + " erscheint!");
        } else {
            headline.setText("A wild " + beast.name() + " appears!");
        }

        return parent;
    }

    @FXML
    public void startFight() {
        //TODO show Encounter screen
    }
}
