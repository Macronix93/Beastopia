package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
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
import javax.inject.Provider;
import java.util.Objects;

public class ChangeBeastElementController extends Controller {
    @FXML
    public ImageView beastImg;
    @FXML
    public Label beastLabel;
    @FXML
    public ProgressBar expProgress;
    @FXML
    public GridPane changeBeastElement;
    @FXML
    public Button addOrRemoveButton;

    @Inject
    PresetsService presetsService;

    @Inject
    Provider<ChangeBeastController> changeBeastControllerProvider;
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

        removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
        addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());

        disposables.add(presetsService.getMonsterType(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    beastLabel.setText(type.name() + " " + type.type() + " Lv. " + monster.level());
                    beastLabel.setStyle("-fx-font-size: 16px");
                }, Throwable::printStackTrace));
        disposables.add(presetsService.getMonsterImage(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(image -> beastImg.setImage(image),
                        Throwable::printStackTrace));

        int maxExp = (int) Math.round(Math.pow(monster.level(), 3) - Math.pow((monster.level() - 1), 3));
        expProgress.setProgress((double) monster.experience() / maxExp);

        teamPane = changeBeastController.beastTeam;
        fightingPane = changeBeastController.currentBeasts;

        if (changeBeastController.getBankMonsters().contains(monster)) {
            addOrRemoveButton.setGraphic(addImage);
        } else {
            addOrRemoveButton.setGraphic(removeImage);
        }

        return parent;
    }

    @FXML
    public void addOrRemoveBeast() {
        if (changeBeastController.getBankMonsters().contains(monster)) {
            changeBeastController.getBankMonsters().remove(monster);
            changeBeastController.getFightingMonsters().add(monster);

            teamPane.getChildren().remove(changeBeastElement);
            fightingPane.getChildren().add(changeBeastElement);
            addOrRemoveButton.setGraphic(removeImage);
        } else {
            changeBeastController.getBankMonsters().add(monster);
            changeBeastController.getFightingMonsters().remove(monster);

            fightingPane.getChildren().remove(changeBeastElement);
            teamPane.getChildren().add(changeBeastElement);
            addOrRemoveButton.setGraphic(addImage);
        }
    }

    private ImageView createImage(String imageUrl) {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setCache(false);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }
}
