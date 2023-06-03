package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MoveTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import de.uniks.beastopia.teaml.utils.Direction;
import de.uniks.beastopia.teaml.utils.PlayerState;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.function.Consumer;

public class EntityController extends Controller {
    int VIEW_SIZE = 40;
    int PORT_WIDTH = 16;
    int PORT_HEIGHT = 32;
    int index = 0;
    @FXML
    public ImageView entityView;
    private Image spriteSheet;
    Trainer trainer;
    Parent parent;
    Direction direction;
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
        disposables.add(udpEventListener.listen("areas." + trainer.area() + ".trainers." + trainer._id() + ".moved", MoveTrainerDto.class)
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
        this.spriteSheet = presetsService.getCharacterSprites(trainer.image()).blockingFirst();
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
        entityView.setFitWidth(VIEW_SIZE);
        entityView.setFitHeight(VIEW_SIZE);
        entityView.setViewport(getViewport());
        return parent;
    }

    private Rectangle2D getViewport() {
        return new Rectangle2D(direction.ordinal() * 96 + index * 16, state.get().ordinal() * 32 + 32, PORT_WIDTH, PORT_HEIGHT);
    }

    private Animation createAnimation() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
