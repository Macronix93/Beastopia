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
import javax.inject.Provider;

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
    @FXML
    public ImageView itemImage;
    @FXML
    public VBox itemImageBox;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<EncounterController> encounterControllerProvider;

    public Monster monster1;
    public Monster monster2;
    private String opponentIdMonsterOne;
    private String opponentIdMonsterTwo;
    ImageView secondMonster;
    HBox selectBox2;
    private EncounterController encounterController;

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

        itemImageBox.toBack();

        if (monster1 != null) {
            disposables.add(presetsService.getMonsterImage(monster1.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        firstMonster.setImage(monsterImage);
                        selectBox.setOnMouseClicked(event -> {
                            if (selectBox2 != null && selectBox2.getStyle().contains("-fx-border-radius: 10px;")) {
                                selectBox2.setStyle("-fx-border-radius: 0px; -fx-alignment: CENTER; -fx-max-height: 125px; -fx-pref-height: 125px; -fx-pref-width: 125px;");
                            }
                            selectBox.setStyle("-fx-border-radius: 10px; -fx-border-color: #000000;");
                            //System.out.println("Chosen Opponent ID: " + opponentIdMonsterOne);
                            encounterController.setChosenTarget(opponentIdMonsterOne);
                        });
                    }));
        }

        if (monster2 != null) {
            secondMonster = new ImageView();
            selectBox2 = new HBox();

            disposables.add(presetsService.getMonsterImage(monster2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        secondMonster.setImage(monsterImage);
                        selectBox2.setStyle("-fx-alignment: CENTER; -fx-max-height: 125px; -fx-pref-height: 125px; -fx-pref-width: 125px;");
                        selectBox2.getChildren().add(secondMonster);
                        monsterContainer.getChildren().addAll(selectBox2);

                        selectBox2.setOnMouseClicked(event -> {
                            if (selectBox.getStyle().contains("-fx-border-radius: 10px;")) {
                                selectBox.setStyle("");
                            }
                            selectBox2.setStyle("-fx-border-radius: 10px; -fx-border-color: #000000; -fx-alignment: CENTER; -fx-max-height: 125px; -fx-pref-height: 125px; -fx-pref-width: 125px;");
                            //System.out.println("Chosen Opponent ID: " + opponentIdMonsterTwo);
                            encounterController.setChosenTarget(opponentIdMonsterTwo);
                        });
                    }));
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
        if (image == null) {
            monsterContainer.getChildren().remove(selectBox);
        }
    }

    public void setImageMonsterTwo(Image image) {
        this.secondMonster.setImage(image);
        if (image == null) {
            monsterContainer.getChildren().remove(selectBox2);
        }
    }

    public void setEncounterController(EncounterController controller) {
        this.encounterController = controller;
    }

    @Override
    public void destroy() {
        super.destroy();

    }

    public void setItemAnimation(Image image) {
        itemImage.setImage(image);
        //TODO und danach wiederr löschen
    }

}