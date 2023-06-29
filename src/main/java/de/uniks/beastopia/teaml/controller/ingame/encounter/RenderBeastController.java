package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class RenderBeastController extends Controller {

    @FXML
    HBox monsterContainer;
    @FXML
    ImageView firstMonster;
    @Inject
    PresetsService presetsService;

    private Monster monster1;
    private Monster monster2;

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

        disposables.add(presetsService.getMonsterImage(monster1.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> firstMonster.setImage(monsterImage)));

        if (monster2 != null) {
            ImageView secondMonster = new ImageView();
            secondMonster.setFitHeight(125);
            secondMonster.setFitWidth(125);
            disposables.add(presetsService.getMonsterImage(monster2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(secondMonster::setImage));
            monsterContainer.getChildren().add(secondMonster);
        }

        return parent;
    }

    public RenderBeastController setBeast1(Monster monster1) {
        this.monster1 = monster1;
        return this;
    }

    public RenderBeastController setBeast2(Monster monster2) {
        this.monster2 = monster2;
        return this;
    }

}
