package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import javax.inject.Provider;

public class MapController extends Controller {
    @FXML
    public Pane mapPane;
    @Inject
    App app;
    @Inject
    DataCache cache;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    private Region region;
    private IngameController backController;

    @Inject
    public MapController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        return super.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void closeMap() {
        IngameController ingameController = ingameControllerProvider.get();
        ingameController.setRegion(region);
        app.show(backController);
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setBackController(IngameController ingameController) {
        this.backController = ingameController;
    }
}
