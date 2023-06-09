package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class RegionInfoController extends Controller {
    @FXML
    public Text place;
    @FXML
    public TextArea description;

    @Inject
    public RegionInfoController() {
    }

    @Override
    public void init() {
        super.init();
        this.place = new Text();
        this.description = new TextArea();
    }

    @Override
    public Parent render() {
        return super.render();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void setPlace(String name) {
        System.out.println("setPlace: " + name);
        this.place.setText(name);
        this.render();
    }

    public void setDescription(String description) {
        System.out.println("setDescription: " + description);
        this.description.setText(description);
    }
}
