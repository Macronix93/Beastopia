package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class EntityController extends Controller {

    @FXML
    public ImageView entityView;
    private Image spriteSheet;
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
        // TODO Subscribe to udpEventListener
        // TODO trainer_image needs to be passed
        this.spriteSheet = presetsService.getSpriteSheet("Prisoner_1_16x16.png").blockingFirst();
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public Parent render() {
        parent = super.render();
        entityView.setPreserveRatio(true);
        entityView.setSmooth(true);
        entityView.setImage(spriteSheet);
        //entityView.setFitWidth(21);
        //entityView.setFitHeight(21);
        entityView.setViewport(new javafx.geometry.Rectangle2D(16, 0, 16, 32));
        return parent;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
