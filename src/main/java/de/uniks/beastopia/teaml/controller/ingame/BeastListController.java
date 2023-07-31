package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BeastListController extends Controller {

    private final List<Monster> monsters = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public VBox VBoxBeasts;
    @FXML
    public VBox VBoxBeastList;
    @FXML
    public Button CloseButtonTestId;
    @Inject
    Provider<BeastController> beastControllerProvider;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;
    private Runnable onCloseRequest;
    private Consumer<Monster> onBeastClicked;

    @Inject
    public BeastListController() {

    }

    public void setOnBeastClicked(Consumer<Monster> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    this.monsters.addAll(monsters);
                    reload();
                }));
        return parent;
    }

    private void reload() {
        for (Monster monster : monsters) {
            BeastController beastController = beastControllerProvider.get()
                    .setBeast(monster);
            beastController.setOnBeastClicked(onBeastClicked);
            beastController.init();
            subControllers.add(beastController);
            Parent parent = beastController.render();
            VBoxBeasts.getChildren().add(parent);
        }
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    @FXML
    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.B)) {
            onCloseRequest.run();
        }
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    public void destroy() {
        super.destroy();
        for (Controller subController : subControllers) {
            subController.destroy();
        }
    }
}
