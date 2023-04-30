package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;

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
        return ("Beastopia - Main Menu");
    }

    @Override
    public Parent render() {
        final Parent parent;
        parent = super.render();
        return parent;
    }

}
