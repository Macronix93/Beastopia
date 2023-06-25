package de.uniks.beastopia.teaml.controller.ingame.encounter;


import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class StartFightNPCController extends Controller {
    @FXML
    public Label headline;
    @FXML
    public ImageView image;

    @Inject
    public StartFightNPCController() {

    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }
        @FXML
    public void startFight() {
    }
}
