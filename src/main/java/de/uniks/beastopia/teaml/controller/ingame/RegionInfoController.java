package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class RegionInfoController extends Controller {
    @FXML
    public VBox rootelement;
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
        this.place = new Text();
        this.description = new Text();
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

    @Override
    public void destroy() {
        super.destroy();
    }

    public RegionInfoController setText(String name, String description) {
        this.name = name;
        this.information = description;
        return this;
    }
}
