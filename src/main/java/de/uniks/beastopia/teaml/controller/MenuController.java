package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class MenuController extends Controller {
    private final Controller regionController;
    private Controller friendListController;
    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;

    @Inject
    public MenuController() {
        regionController = new RegionController();
    }

    @Override
    public String getTitle() {
        return "Beastopia - Main Menu";
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        regionContainer.getChildren().add(regionController.render());
        return parent;
    }

}
