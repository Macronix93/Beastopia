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
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EntityController extends Controller {
    private static final Map<String, Image> SPRITESHEET = new HashMap<>();
    int PORT_WIDTH = 16;
    int PORT_HEIGHT = 32;
    int SPRITE_STEP = 16;
    int STATE_STEP = 32;
    int DIRECTION_STEP = 96;
    int index = 0;
    Trainer trainer;
    Parent parent;
    Direction direction;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Consumer<MoveTrainerDto> onTrainerUpdate;

    @FXML
    public ImageView entityView;
    @Inject
    PresetsService presetsService;
    @Inject
    UDPEventListener udpEventListener;


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
        listenToMovements(trainer.area());
    }

    private void listenToMovements(String areaID) {
        disposables.add(udpEventListener.listen("areas." + areaID + ".trainers." + trainer._id() + ".moved", MoveTrainerDto.class)
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
                            if (!event.data().area().equals(trainer.area())) {
                                trainer = new Trainer(
                                        trainer.createdAt(), trainer.updatedAt(), trainer._id(), trainer.region(),
                                        trainer.user(), trainer.name(), trainer.image(), trainer.coins(),
                                        event.data().area(), trainer.x(), trainer.y(), trainer.direction(),
                                        trainer.npc());
                                listenToMovements(event.data().area());
                            }
                            onTrainerUpdate.accept(event.data());
                        },
                        error -> {
                            throw new RuntimeException(error);
                        }
                ));
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return this.trainer;
    }

    @Override
    public Parent render() {
        int VIEW_SIZE = 40;
        parent = super.render();
        entityView.toFront();
        entityView.setPreserveRatio(true);
        entityView.setSmooth(true);
        entityView.setFitWidth(VIEW_SIZE);
        entityView.setFitHeight(VIEW_SIZE);
        entityView.setViewport(getViewport());

        if (!SPRITESHEET.containsKey(trainer.image())) {
            disposables.add(presetsService.getCharacterSprites(trainer.image())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(image -> {
                        SPRITESHEET.put(trainer.image(), image);
                        entityView.setImage(image);
                    }));
        } else {
            entityView.setImage(SPRITESHEET.get(trainer.image()));
        }
        return parent;
    }

    private Rectangle2D getViewport() {
        return new Rectangle2D(direction.ordinal() * DIRECTION_STEP + index * SPRITE_STEP,
                state.get().ordinal() * STATE_STEP + STATE_STEP,
                PORT_WIDTH,
                PORT_HEIGHT);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
