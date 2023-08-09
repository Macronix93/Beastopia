package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Timer;
import java.util.TimerTask;

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
    VBox selectBox;
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
    VBox selectBox2;
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
                                selectBox2.setStyle("-fx-border-radius: 0px; -fx-alignment: BOTTOM_CENTER; -fx-max-height: 150px; -fx-pref-height: 150px; -fx-pref-width: 125px;");
                            }
                            selectBox.setStyle("-fx-border-radius: 10px; -fx-border-color: #000000;");
                            encounterController.setChosenTarget(opponentIdMonsterOne);
                        });
                    }));
        }

        if (monster2 != null) {
            secondMonster = new ImageView();
            selectBox2 = new VBox();

            disposables.add(presetsService.getMonsterImage(monster2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> {
                        secondMonster.setImage(monsterImage);
                        selectBox2.setStyle("-fx-alignment: BOTTOM_CENTER; -fx-max-height: 150px; -fx-pref-height: 150px; -fx-pref-width: 125px;");
                        selectBox2.getChildren().add(secondMonster);
                        monsterContainer.getChildren().addAll(selectBox2);

                        selectBox2.setOnMouseClicked(event -> {
                            if (selectBox.getStyle().contains("-fx-border-radius: 10px;")) {
                                selectBox.setStyle("");
                            }
                            selectBox2.setStyle("-fx-border-radius: 10px; -fx-border-color: #000000; -fx-alignment: BOTTOM_CENTER; -fx-max-height: 150px; -fx-pref-height: 150px; -fx-pref-width: 125px;");
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

    public void setItemAnimation(Image image, VBox selectBox) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        if (selectBox != null) {
            selectBox.getChildren().add(0, imageView);
            imageView.setImage(image);
        } else {
            itemImage.setImage(image);
        }
        // Timer to remove the shown image
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (selectBox != null) {
                        imageView.setImage(null);
                        selectBox.getChildren().remove(imageView);
                    } else {
                        itemImage.setImage(null);
                    }
                });
            }
        }, 2000);
    }

    public void removeSelectBox() {
        selectBox.setStyle("-fx-alignment: BOTTOM_CENTER; -fx-max-height: 150px; -fx-pref-height: 150px; -fx-pref-width: 125px;");
        if (selectBox2 != null) {
            selectBox2.setStyle("-fx-alignment: BOTTOM_CENTER; -fx-max-height: 150px; -fx-pref-height: 150px; -fx-pref-width: 125px;");
        }
    }
}