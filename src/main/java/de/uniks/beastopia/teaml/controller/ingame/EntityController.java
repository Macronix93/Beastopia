package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MoveTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.function.Consumer;

public class EntityController extends Controller {

    @FXML
    public ImageView entityView;
    private Image spriteSheet;
    Trainer trainer = new Trainer(null, null, "646c84a0f148f6eb461bf654", null, null, null, "Prisoner_1_16x16.png", 0, "645e32c6866ace359554a7fa"
            , 0, 0, 0, null);
    Parent parent;

    @Inject
    PresetsService presetsService;

    @Inject
    UDPEventListener udpEventListener;

    Consumer<MoveTrainerDto> onTrainerUpdate;

    @Inject
    public EntityController() {
    }

    public void setOnTrainerUpdate(Consumer<MoveTrainerDto> onTrainerUpdate) {
        this.onTrainerUpdate = onTrainerUpdate;
    }

    @Override
    public void init() {
        super.init();
        disposables.add(udpEventListener.listen("areas.645e32c6866ace359554a7fa.trainers.646c84a0f148f6eb461bf654.moved", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        event -> onTrainerUpdate.accept(event.data()),
                        error -> {
                            throw new RuntimeException(error);
                        }
                ));
        // TODO trainer_image needs to be passed
        this.spriteSheet = presetsService.getSpriteSheet(trainer.image()).blockingFirst();
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public Parent render() {
        parent = super.render();
        entityView.toFront();
        entityView.setPreserveRatio(true);
        entityView.setSmooth(true);
        entityView.setImage(spriteSheet);
        entityView.setFitWidth(21);
        entityView.setFitHeight(21);
        entityView.setViewport(new javafx.geometry.Rectangle2D(0, 64, 16, 32));
        return parent;
    }

    // TODO
    // Animation
    //


    @Override
    public void destroy() {
        super.destroy();
    }
}
