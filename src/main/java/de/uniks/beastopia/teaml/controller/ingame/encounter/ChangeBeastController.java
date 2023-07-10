package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangeBeastController extends Controller {
    @FXML
    public GridPane yourBeastsPane;
    @FXML
    public GridPane fightingBeastPane;
    @FXML
    public VBox currentBeasts;
    @FXML
    public VBox beastTeam;
    public ImageView removeImage;
    public ImageView addImage;

    @Inject
    EncounterOpponentsService encounterOpponentsService;
    @Inject
    PresetsService presetsService;
    @Inject
    TrainerService trainerService;
    @Inject
    DataCache cache;
    @Inject
    Provider<ChangeBeastElementController> changeBeastElementControllerProvider;

    private final List<Monster> playerMonsters = new ArrayList<>();
    private final List<Monster> fightingMonsters = new ArrayList<>();
    private final List<Monster> bankMonsters = new ArrayList<>();
    private Monster currentMonster;
    private EncounterController encounterController;
    private LoadingPage loadingPage;

    @Inject
    public ChangeBeastController() {
    }

    public void setCurrentMonster(Monster currentMonster) {
        this.currentMonster = currentMonster;
    }

    public void setEncounterController(EncounterController controller) {
        this.encounterController = controller;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public String getTitle() {
        return resources.getString("titleChangeBeasts");
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
        addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());

        disposables.add(trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    playerMonsters.addAll(monsters);

                    for (Monster monster : playerMonsters) {
                        ChangeBeastElementController controller = changeBeastElementControllerProvider.get()
                                .setMonster(monster)
                                .setParentController(this)
                                .setIcons(removeImage, addImage);

                        if (monster._id().equals(currentMonster._id())) {
                            currentBeasts.getChildren().add(controller.render());
                            controller.addOrRemoveButton.setGraphic(removeImage);

                            fightingMonsters.add(monster);
                        } else {
                            beastTeam.getChildren().add(controller.render());
                            controller.addOrRemoveButton.setGraphic(addImage);

                            bankMonsters.add(monster);
                        }
                    }

                    loadingPage.setDone();
                }));

        return loadingPage.parent();
    }

    @FXML
    public void back() {
        app.show(encounterController);
    }

    @SuppressWarnings("CommentedOutCode")
    @FXML
    public void changeBeast() {
        if (fightingMonsters.isEmpty()) {
            System.out.println("no monster selected!");
        } else if (fightingMonsters.size() > 1) {
            System.out.println("there are two monsters selected!");
        } else if (fightingMonsters.get(0).currentAttributes().health() == 0) {
            System.out.println("current monster has no health points left!");
        } else if (currentMonster._id().equals(fightingMonsters.get(0)._id())) {
            System.out.println("current monster is the same as initial monster!");
        } else {
            //TODO: Apply changes and send request to server
            System.out.println("current monster: " + fightingMonsters.get(0)._id());

            encounterController.setOwnMonster(fightingMonsters.get(0));
            app.show(encounterController);

            /*disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getCurrentOpponents().get(1)._id(),
                            currentMonster._id(), new ChangeMonsterMove("change-monster", fightingMonsters.get(0)._id()))
                    .subscribe(update -> {
                        System.out.println(update.monster());
                        encounterController.setOwnMonster(fightingMonsters.get(0));
                        app.show(encounterController);
                    }, Throwable::printStackTrace));*/
        }
    }

    public List<Monster> getFightingMonsters() {
        return this.fightingMonsters;
    }

    public List<Monster> getBankMonsters() {
        return this.bankMonsters;
    }

    private ImageView createImage(String imageUrl) {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setCache(false);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }
}
