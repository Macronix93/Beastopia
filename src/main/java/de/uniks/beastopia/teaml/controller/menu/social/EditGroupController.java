package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.scene.Parent;

import javax.inject.Inject;

public class EditGroupController extends Controller {

    @Inject
    public EditGroupController() {

    }

    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditGroup");
    }
}
