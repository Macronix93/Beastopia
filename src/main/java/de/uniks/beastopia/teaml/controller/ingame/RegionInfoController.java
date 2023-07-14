package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class RegionInfoController extends Controller {
    @FXML
    public Text place;
    @FXML
    public Text description;

    String name;
    String information;

    @Inject
    public RegionInfoController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        this.place.setText(name);
        if (information.equals("")) {
            this.description.visibleProperty().setValue(false);
        } else {
            this.description.setText(information);
        }
        return parent;
    }

    public void setText(String name, String description) {
        this.name = name;
        this.information = description;
    }
}
