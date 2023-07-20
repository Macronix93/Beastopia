package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class RenderBeastController extends Controller {

    @FXML
    VBox monsterRenderBox;
    @FXML
    Ellipse ellipse;
    @FXML
    HBox monsterContainer;
    @FXML
    ImageView firstMonster;
    @Inject
    PresetsService presetsService;

    public Monster monster1;
    public Monster monster2;

    @Inject
    public RenderBeastController() {
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public void onResize(int width, int height) {
        ellipse.setRadiusX(width / 5);
        ellipse.setRadiusY(height / 6);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(presetsService.getMonsterImage(monster1.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> firstMonster.setImage(monsterImage)));

        if (monster2 != null) {
            ImageView secondMonster = new ImageView();

            disposables.add(presetsService.getMonsterImage(monster2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(secondMonster::setImage));
            monsterContainer.getChildren().add(secondMonster);
        }

        //ellipse.radiusXProperty().bind(monsterRenderBox.widthProperty().divide(2));
        //ellipse.radiusYProperty().bind(monsterRenderBox.heightProperty().divide(2));

        return parent;
    }

    public RenderBeastController setMonster1(Monster monster1) {
        this.monster1 = monster1;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public RenderBeastController setMonster2(Monster monster2) {
        this.monster2 = monster2;
        return this;
    }

    @Override
    public void destroy() {
        super.destroy();

    }

}
