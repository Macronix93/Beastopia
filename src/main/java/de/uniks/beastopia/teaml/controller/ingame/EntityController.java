package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MoveTrainerDto;
import de.uniks.beastopia.teaml.rest.Position;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Direction;
import de.uniks.beastopia.teaml.utils.PlayerState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.function.Consumer;

public class EntityController extends Controller {
    final int PORT_WIDTH = 16;
    final int PORT_HEIGHT = 32;
    final int SPRITE_STEP = 16;
    final int STATE_STEP = 32;
    final int DIRECTION_STEP = 96;
    int index = 0;
    Trainer trainer;
    Parent parent;
    Direction direction;
    final ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Consumer<MoveTrainerDto> onTrainerUpdate;

    @FXML
    public ImageView entityView;
    @Inject
    DataCache cache;
    private Position position;

    @Inject
    public EntityController() {
    }

    public void setOnTrainerUpdate(Consumer<MoveTrainerDto> onTrainerUpdate) {
        this.onTrainerUpdate = onTrainerUpdate;
    }

    public ObjectProperty<PlayerState> playerState() {
        return state;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public void init() {
        super.init();
        direction = Direction.DOWN;
    }

    public void updateTrainer(MoveTrainerDto data) {
        position = new Position(data.x(), data.y());
        setDirection(data.direction());
        index = (index + 1) % 6;
        updateViewPort();
        if (!data.area().equals(trainer.area())) {
            trainer = new Trainer(trainer.createdAt(), trainer.updatedAt(), trainer._id(), trainer.region(),
                    trainer.user(), trainer.name(), trainer.image(), trainer.team(), trainer.visitedAreas(), trainer.coins(), data.area(), trainer.x(),
                    trainer.y(), trainer.direction(), trainer.npc());
        }
        onTrainerUpdate.accept(data);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return this.trainer;
    }

    public void updateViewPort() {
        entityView.setViewport(getViewport());
    }

    @Override
    public Parent render() {
        int VIEW_SIZE = 96;
        if (state.get().equals(PlayerState.JUMP)) {
            VIEW_SIZE = (int) (VIEW_SIZE * 1.2);
        }
        parent = super.render();
        entityView.toFront();
        entityView.setPreserveRatio(true);
        entityView.setSmooth(true);
        entityView.setFitWidth(VIEW_SIZE);
        entityView.setFitHeight(VIEW_SIZE);
        updateViewPort();

        disposables.add(cache.getOrLoadTrainerImage(trainer.image(), true).observeOn(FX_SCHEDULER).subscribe(image -> entityView.setImage(image)));

        entityView.setPickOnBounds(false);
        parent.setPickOnBounds(false);

        return parent;
    }

    private Rectangle2D getViewport() {
        int SPRITE_SCALING = 5;
        int sheetY = state.get().equals(PlayerState.JUMP) ? 1 : state.get().ordinal();

        return new Rectangle2D(
                (direction.ordinal() * DIRECTION_STEP + index * SPRITE_STEP) * SPRITE_SCALING,
                (sheetY * STATE_STEP + STATE_STEP) * SPRITE_SCALING,
                PORT_WIDTH * SPRITE_SCALING,
                PORT_HEIGHT * SPRITE_SCALING);
    }

    public void setDirection(int direction) {
        switch (direction) {
            case 0 -> this.direction = Direction.RIGHT;
            case 1 -> this.direction = Direction.UP;
            case 2 -> this.direction = Direction.LEFT;
            case 3 -> this.direction = Direction.DOWN;
        }
    }
}
