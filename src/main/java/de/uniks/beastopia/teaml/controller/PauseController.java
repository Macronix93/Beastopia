package de.uniks.beastopia.teaml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class PauseController extends Controller {

    @FXML
    private Button editProfileButton;
    @FXML
    private Button backToMainButton;

    @Inject
    public PauseController() {

    }

    @Override
    public String getTitle() {
        return "Beastopia - Pause Menu";
    }

    public void editProfileButtonPressed(ActionEvent actionEvent) {
    }

    public void mainMenuButtonPressed(ActionEvent actionEvent) {
    }
}
