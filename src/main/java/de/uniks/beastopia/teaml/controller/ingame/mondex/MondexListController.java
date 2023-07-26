package de.uniks.beastopia.teaml.controller.ingame.mondex;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.MondexService;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MondexListController extends Controller {

    private final List<MonsterTypeDto> monsters = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    public VBox VBoxMondexList;
    public VBox VBoxBeasts;
    @Inject
    Provider<MondexElementController> mondexElementControllerProvider;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache dataCache;
    @Inject
    MondexService mondexService;

    private Runnable onCloseRequest;
    private Consumer<MonsterTypeDto> onBeastClicked;

    @Inject
    public MondexListController() {

    }

    @Override
    public void init() {
        disposables.add(presetsService.getAllBeasts()
                .observeOn(FX_SCHEDULER)
                .subscribe(this.monsters::addAll));
    }

    public void setOnBeastClicked(Consumer<MonsterTypeDto> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        mondexService.listMonsterTypes(monsters);

        VBoxBeasts.getChildren().clear();
        for (MonsterTypeDto monster : monsters) {
            MondexElementController mondexElementController = mondexElementControllerProvider.get()
                    .setMonster(monster, mondexService.checkKnown(monster.id()));
            mondexElementController.setOnBeastClicked(onBeastClicked);
            mondexElementController.init();
            subControllers.add(mondexElementController);
            Parent render = mondexElementController.render();
            VBoxBeasts.getChildren().add(render);
        }

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

    public void reload() {
        render();
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
