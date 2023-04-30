package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;
import java.io.IOException;

public class IngameController extends Controller {

    @FXML
    private Button pause;

    public void pauseMenu() {
        //ToDo Show Pause Menu
    }

    public String getTitle() {
        return ("Beastopia");
    }

    @Inject
    public IngameController() {
    }

    @Override
    public Parent render() {
        final Parent parent;
        try {
            parent = super.render();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return parent;
    }
}
