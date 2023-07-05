package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.util.ArrayList;

public class EndScreenController extends Controller {

    @FXML
    Button leaveButton;
    @FXML
    Label resultLabel;

    private Monster winner1;
    private Monster winner2;
    private Monster loser1;
    private Monster loser2;

    @Inject
    public EndScreenController() {
    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }

    public void setWinner1(Monster winner1) {
        this.winner1 = winner1;
    }

    public void setWinner2(Monster winner2) {
        this.winner2 = winner2;
    }

    public void setLoser1(Monster loser1) {
        this.loser1 = loser1;
    }

    public void setLoser2(Monster loser2) {
        this.loser2 = loser2;
    }

}
