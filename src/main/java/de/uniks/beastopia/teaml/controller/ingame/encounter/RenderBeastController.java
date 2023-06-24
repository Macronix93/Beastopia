package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class RenderBeastController extends Controller {

    ArrayList<Monster> monsters;

    @FXML
    HBox monsterContainer;
    @FXML
    ImageView firstMonster;

    @Inject
    public RenderBeastController() {
    }

    @Override
    public void init() {
        //TODO: get monsters, check number of monsters
        /*
        if (monsters.size() >= 1) {
            //get ImgView of seconds monster, set width and height to 125
            monsterContainer.getChildren().add( ImageView of seconds monster );

        }
         */
    }

    @Override
    public Parent render() {
        Parent parent = super.render();


        return parent;
    }

}
