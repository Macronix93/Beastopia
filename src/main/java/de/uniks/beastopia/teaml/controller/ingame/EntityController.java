package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class EntityController extends Controller {

    @FXML
    public ImageView entityView;
    Trainer trainer;
    Parent parent;

    @Inject
    TrainerService trainerService;

    @Inject
    PresetsService presetsService;

    @Inject
    UDPEventListener udpEventListener;

    @Inject
    public EntityController() {
    }

    @Override
    public void init() {
        super.init();
        disposables.add(presetsService.getSpriteSheet(trainer.image())
                .observeOn(FX_SCHEDULER)
                .subscribe(spriteSheet -> this.entityView = new ImageView(spriteSheet)));
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public Parent render() {
        parent = super.render();

        entityView.setViewport(new javafx.geometry.Rectangle2D(0, 0, 20, 20));
        return parent;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
