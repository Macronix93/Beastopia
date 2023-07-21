package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MondexListController extends Controller {

    private final List<Monster> monsters = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();

    @Inject
    public MondexListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
    }
}
