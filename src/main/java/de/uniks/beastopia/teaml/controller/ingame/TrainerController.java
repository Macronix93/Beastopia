package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.trainer.Trainer;
import de.uniks.beastopia.teaml.service.TrainerService;
import javafx.scene.Parent;

import javax.inject.Inject;

public class TrainerController extends Controller {

    Trainer trainer;

    @Inject
    TrainerService trainerService;

    @Inject
    public TrainerController() {}

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
    @Override
    public Parent render() {
        return super.render();
    }
}
