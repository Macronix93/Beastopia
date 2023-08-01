package de.uniks.beastopia.teaml.controller.ingame.mondex;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.MondexService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MondexListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    private final Map<Integer, Parent> monsterMap = new HashMap<>();
    public VBox VBoxMondexList;
    public VBox VBoxBeasts;
    @Inject
    Provider<MondexElementController> mondexElementControllerProvider;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache dataCache;
    @Inject
    TrainerService trainerService;
    @Inject
    MondexService mondexService;

    private Runnable onCloseRequest;
    private Consumer<MonsterTypeDto> onBeastClicked;

    @Inject
    public MondexListController() {

    }

    @Override
    public void init() {
        super.init();
        mondexService.init();
    }

    public void setOnBeastClicked(Consumer<MonsterTypeDto> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        VBoxBeasts.getChildren().clear();

        disposables.add(trainerService.getTrainer(dataCache.getTrainer().region(), dataCache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(newTrainer -> {
                    dataCache.setTrainer(newTrainer);
                    mondexService.setTrainer(newTrainer);
                    disposables.add(presetsService.getAllBeasts()
                            .subscribe(monsters -> { //No UI Stuff here!!!
                                for (MonsterTypeDto monster : monsters) {
                                    Thread.sleep(288);
                                    if (dataCache.imageIsDownloaded(monster.id())) {
                                        disposables.add(delay().observeOn(FX_SCHEDULER).subscribe(t ->
                                        {
                                            createController(monster);
                                            System.out.println("already loaded");
                                        }));
                                        continue;
                                    }
                                    disposables.add(presetsService.getMonsterImage(monster.id())
                                            .observeOn(FX_SCHEDULER)
                                            .subscribe(image -> { //UI Stuff allowed again!!!
                                                dataCache.addMonsterImages(monster.id(), image);
                                                createController(monster);
                                                System.out.println("downloaded");
                                            }));
                                }
                            }));
                }));

        return parent;
    }

    private void createController(MonsterTypeDto monster) {
        MondexElementController mondexElementController = mondexElementControllerProvider.get()
                .setMonster(monster, mondexService.checkKnown(monster.id()));
        mondexElementController.setOnBeastClicked(onBeastClicked);
        mondexElementController.init();
        subControllers.add(mondexElementController);
        Parent render = mondexElementController.render();
        monsterMap.put(monster.id(), render);
        reloadMap();
    }

    private void reloadMap() {
        VBoxBeasts.getChildren().clear();
        monsterMap.keySet().stream().sorted().forEachOrdered(key -> VBoxBeasts.getChildren().add(monsterMap.get(key)));
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.L)) {
            close();
        }
    }

    public void close() {
        onCloseRequest.run();
    }
}
