package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MoveTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import de.uniks.beastopia.teaml.utils.Direction;
import de.uniks.beastopia.teaml.utils.PlayerState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    Direction direction;
    int index = 0;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();

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

    public ObjectProperty<PlayerState> playerState() {
        return state;
    }

    @Override
    public void init() {
        super.init();
        direction = Direction.DOWN;
        disposables.add(udpEventListener.listen("areas.645e32c6866ace359554a7fa.trainers.646c84a0f148f6eb461bf654.moved", MoveTrainerDto.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        event -> {
                            switch (event.data().direction()) {
                                case 0 -> direction = Direction.RIGHT;
                                case 1 -> direction = Direction.UP;
                                case 2 -> direction = Direction.LEFT;
                                case 3 -> direction = Direction.DOWN;
                            }
                            index = (index + 1) % 6;
                            this.render();
                            onTrainerUpdate.accept(event.data());
                        },
                        error -> {
                            throw new RuntimeException(error);
                        }
                ));
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
        entityView.setFitWidth(40);
        entityView.setFitHeight(40);
        entityView.setViewport(
                new javafx.geometry.Rectangle2D(direction.ordinal() * 96 + index * 16
                        , state.get().ordinal() * 32 + 32, 16, 32));
        return parent;
    }


    @Override
    public void destroy() {
        super.destroy();
    }
}
