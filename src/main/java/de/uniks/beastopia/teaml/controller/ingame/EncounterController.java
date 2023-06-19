package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.scene.Parent;

import javax.inject.Inject;

public class EncounterController extends Controller {

    @Inject
    public EncounterController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

}
