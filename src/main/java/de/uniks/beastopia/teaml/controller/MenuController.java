package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class MenuController extends Controller {
    @Inject
    Provider<RegionController> regionControllerProvider;
    private Controller friendListController;
    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;

    List<Controller> subControllers = new ArrayList<>();

    @Inject
    public MenuController() {

    }

    @Override
    public String getTitle() {
        return "Beastopia - Main Menu";
    }

    @Override
    public void init() {
        super.init();
        subControllers.add(regionControllerProvider.get());
        onDestroy(() -> subControllers.forEach(Controller::destroy));
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        regionContainer.getChildren().add(subControllers.get(0).render());
        return parent;
    }

}
