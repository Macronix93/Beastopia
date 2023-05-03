package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.io.IOException;

public class IngameController extends Controller {

    @FXML
    public HBox ingame;
    @FXML
    private Button pause;

    @Inject
    public IngameController() {
    }

    public void pauseMenu() {
        //ToDo Show Pause Menu
    }

    @Override
    public String getTitle() {
        return "Beastopia";
    }
}
