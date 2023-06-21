package de.uniks.beastopia.teaml.controller.ingame.encouter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class FightWildBeastController extends Controller {

    @Inject
    public Label headline;
    @Inject
    public ImageView image;
    @Inject
    public Button startFight;

    @Inject
    public FightWildBeastController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @Inject
    public void startFight(ActionEvent actionEvent) {
    }
}
