package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class BeastController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label lastname;
    @FXML
    public Label hp;
    @FXML
    public Label level;
    private Monster monster;
    private Consumer<Monster> onBeastClicked;

    @Inject
    public BeastController() {

    }

    public void setOnBeastClicked(Consumer<Monster> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
    }

    public BeastController setBeast(Monster monster) {
        this.monster = monster;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        return parent;
    }

    @FXML
    public void toggleDetails() {
        onBeastClicked.accept(monster);
    }
}
