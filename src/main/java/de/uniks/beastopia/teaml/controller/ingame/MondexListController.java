package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class MondexListController extends Controller {

    private final List<Monster> monsters = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    @Inject
    Provider<MondexElementController> mondexElementControllerProvider;
    public VBox VBoxMondexList;
    public VBox VBoxBeasts;
    private Runnable onCloseRequest;

    @Inject
    public MondexListController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        VBoxBeasts.getChildren().clear();
        VBoxBeasts.getChildren().add(mondexElementControllerProvider.get().render());
        return parent;
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    @Override
    public void destroy() {
        super.destroy();
        subControllers.forEach(Controller::destroy);
    }

    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.X)) {
            close();
        }
    }

    public void close() {
        onCloseRequest.run();
    }
}
