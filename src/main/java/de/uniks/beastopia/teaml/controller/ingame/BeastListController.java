package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BeastListController extends Controller {

    @FXML
    public VBox VBoxBeasts;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<BeastController> beastControllerProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;

    private Runnable onCloseRequest;
    private final List<Monster> monsters = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
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
                    // TODO: change back to monsters
                    List<Monster> tmpMonsters = List.of(
                            new Monster(null, null, "3", "TR_1", 2, 5, 10,
                                    new MonsterAttributes(10,10,10,10),
                                    new MonsterAttributes(5, 5, 2, 5))
                    );
                    this.monsters.addAll(tmpMonsters);
                    reload();
                }));
        return parent;
    }

    private void reload() {
        for (Monster monster : monsters) {
            BeastController beastController = new BeastController()
                    .setBeast(monster);
            beastController.setOnBeastClicked(onBeastClicked);
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
}
