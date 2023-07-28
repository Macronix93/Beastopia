package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
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
    @FXML
    HBox selectBox;
    @Inject
    PresetsService presetsService;

    private Monster monster1;
    private Monster monster2;
    private String opponentIdMonsterOne;
    private String opponentIdMonsterTwo;
    private ImageView secondMonster;

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
                .subscribe(monsterImage -> {
                    firstMonster.setImage(monsterImage);
                    firstMonster.setOnMouseClicked(event -> System.out.println("Opponent ID: " + opponentIdMonsterOne));
                }));

        if (monster2 != null) {
            secondMonster = new ImageView();

            disposables.add(presetsService.getMonsterImage(monster2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        secondMonster.setImage(monsterImage);
                        secondMonster.setOnMouseClicked(event -> System.out.println("Opponent ID: " + opponentIdMonsterTwo));
                    }));
            monsterContainer.getChildren().add(secondMonster);
        }

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

    public void setMonsterOneOpponentId(String id) {
        this.opponentIdMonsterOne = id;
    }

    public void setMonsterTwoOpponentId(String id) {
        this.opponentIdMonsterTwo = id;
    }

    public String getOpponentIdMonsterOne() {
        return opponentIdMonsterOne;
    }

    public String getOpponentIdMonsterTwo() {
        return opponentIdMonsterTwo;
    }

    public void setImageMonsterOne(Image image) {
        this.firstMonster.setImage(image);
    }

    public void setImageMonsterTwo(Image image) {
        this.secondMonster.setImage(image);
    }

    @Override
    public void destroy() {
        super.destroy();

    }

}