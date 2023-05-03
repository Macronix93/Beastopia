package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javax.inject.Inject;

public class MenuController extends Controller {
    private Controller regionController;
    private Controller friendListController;
    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;

    @Inject
    public MenuController() {

    }

    public String getTitle() {
        return "Beastopia - Main Menu";
    }

}
