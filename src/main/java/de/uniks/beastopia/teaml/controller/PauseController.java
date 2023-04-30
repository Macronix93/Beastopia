package de.uniks.beastopia.teaml.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import java.io.IOException;

public class PauseController extends Controller {

    @FXML
    private Button editProfileButton;
    @FXML
    private Button backToMainButton;
    @Inject
    public PauseController() {

    }
    public String getTitle() {
        return ("Beastopia - Pause Menu");
    }
    @Override
    public Parent render() {
        final Parent parent;
        parent = super.render();
        return parent;
    }

    public void editProfileButtonPressed(ActionEvent actionEvent) {
    }

    public void mainMenuButtonPressed(ActionEvent actionEvent) {
    }
}
