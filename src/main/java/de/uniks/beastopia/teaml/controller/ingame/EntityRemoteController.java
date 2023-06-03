package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MoveTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.utils.Direction;
import de.uniks.beastopia.teaml.utils.PlayerState;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.function.Consumer;

public class EntityRemoteController extends Controller {
    @SuppressWarnings("unused")
    int VIEW_SIZE = 40;
    int PORT_WIDTH = 16;
    int PORT_HEIGHT = 32;
    int index = 0;

    @SuppressWarnings("unused")
    Trainer trainer;
    Parent parent;
    Direction direction;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Consumer<MoveTrainerDto> onRemoteTrainerUpdate;
    @SuppressWarnings("unused")
    ArrayList<Trainer> trainers = new ArrayList<>();

    @Inject
    public EntityRemoteController() {
    }

    @SuppressWarnings("unused")
    private void setOnRemoteTrainerUpdate(Consumer<MoveTrainerDto> onRemoteTrainerUpdate) {
        this.onRemoteTrainerUpdate = onRemoteTrainerUpdate;
    }

    @SuppressWarnings("unused")
    private void getAllTrainers() {
        // TODO get all trainers from area via UDP eventlistener
    }

    @Override
    public void init() {
        super.init();


    }


    @Override
    public Parent render() {
        parent = super.render();

        return parent;
    }

    @SuppressWarnings("unused")
    private Rectangle2D getViewport() {
        return new Rectangle2D(direction.ordinal() * 96 + index * 16, state.get().ordinal() * 32 + 32, PORT_WIDTH, PORT_HEIGHT);
    }

    @SuppressWarnings("unused")
    private Animation createAnimation() {
        return null;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

}


