package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.RegionService;
import de.uniks.beastopia.teaml.utils.LoadingPage;
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
    PresetsService presetsService;
    @Inject
    RegionService regionService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    private Region region;
    private IngameController backController;
    private LoadingPage laodingPage;

    // TODO: remove this backpass
    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    public MapController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        laodingPage = LoadingPage.makeLoadingPage(super.render());
        disposables.add(regionService.getRegion(cache.getJoinedRegion()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(region -> {
                            this.region = region;
                            System.out.println(region);
                            laodingPage.setDone();
                        }
                ));
        return laodingPage.parent();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void closeMap() {
        IngameController ingameController = ingameControllerProvider.get();
        ingameController.setRegion(region);

        // TODO: remove this backpass
        MenuController menuController = menuControllerProvider.get();
        app.show(backController);
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setBackController(IngameController ingameController) {
        this.backController = ingameController;
    }
}
