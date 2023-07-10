package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ChangeBeastElementController extends Controller {
    @FXML
    public ImageView beastImg;
    @FXML
    public Label beastLabel;
    @FXML
    public ProgressBar expProgress;
    @FXML
    public GridPane gridPane;
    @FXML
    public Button addOrRemoveButton;

    @Inject
    PresetsService presetsService;

    ChangeBeastController changeBeastController;

    private Monster monster;
    private VBox teamPane;
    private VBox fightingPane;
    private ImageView removeImage;
    private ImageView addImage;

    @Inject
    public ChangeBeastElementController() {
    }

    @Override
    public void init() {
        super.init();
    }

    public ChangeBeastElementController setMonster(Monster monster) {
        this.monster = monster;
        return this;
    }

    public ChangeBeastElementController setParentController(ChangeBeastController controller) {
        this.changeBeastController = controller;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        teamPane = changeBeastController.beastTeam;
        fightingPane = changeBeastController.currentBeasts;
        removeImage = changeBeastController.removeImage;
        addImage = changeBeastController.addImage;

        disposables.add(presetsService.getMonsterType(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> beastLabel.setText(type.name() + " " + type.type() + " Lv. " + monster.level()), Throwable::printStackTrace));
        disposables.add(presetsService.getMonsterImage(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(image -> beastImg.setImage(image),
                        Throwable::printStackTrace));

        int maxExp = (int) Math.round(Math.pow(monster.level(), 3) - Math.pow((monster.level() - 1), 3));
        expProgress.setProgress((double) monster.experience() / maxExp);

        return parent;
    }

    @FXML
    public void addOrRemoveBeast() {
        if (changeBeastController.getBankMonsters().contains(monster)) {
            changeBeastController.getBankMonsters().remove(monster);
            changeBeastController.getFightingMonsters().add(monster);

            teamPane.getChildren().remove(gridPane);
            fightingPane.getChildren().add(gridPane);
            addOrRemoveButton.setGraphic(removeImage);
        } else {
            changeBeastController.getBankMonsters().add(monster);
            changeBeastController.getFightingMonsters().remove(monster);

            fightingPane.getChildren().remove(gridPane);
            teamPane.getChildren().add(gridPane);
            addOrRemoveButton.setGraphic(addImage);
        }

        /*if (teamPane.getChildren().contains(gridPane)) {
            changeBeastController.getBankMonsters().remove(monster);
            changeBeastController.getFightingMonsters().add(monster);

            teamPane.getChildren().remove(gridPane);
            fightingPane.getChildren().add(gridPane);
            addOrRemoveButton.setGraphic(removeImage);
        } else {
            changeBeastController.getBankMonsters().add(monster);
            changeBeastController.getFightingMonsters().remove(monster);

            fightingPane.getChildren().remove(gridPane);
            teamPane.getChildren().add(gridPane);
            addOrRemoveButton.setGraphic(addImage);
        }*/

        System.out.println("Fighting Monsters: " + changeBeastController.getFightingMonsters() + " | Bank monsters: " + changeBeastController.getBankMonsters());
    }
}
