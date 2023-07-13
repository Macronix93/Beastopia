package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ChangeMonsterMove;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
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
                        System.out.println("monster " + monster._id() + " hp: " + monster.currentAttributes().health());

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

    @FXML
    public void changeBeast() {
        if (fightingMonsters.isEmpty()) {
            Dialog.error(resources.getString("error"), resources.getString("noMonSelected"));
        } else if (fightingMonsters.size() > 1) {
            Dialog.error(resources.getString("error"), resources.getString("twoMonSelected"));
        } else if (fightingMonsters.get(0).currentAttributes().health() == 0) {
            Dialog.error(resources.getString("error"), resources.getString("monNoHPLeft"));
        } else if (currentMonster._id().equals(fightingMonsters.get(0)._id())) {
            Dialog.error(resources.getString("error"), resources.getString("currentMonIsSame"));
        } else {
            disposables.add(encounterOpponentsService.getTrainerOpponents(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                    .subscribe(o -> {
                                if (o.get(0).monster() == null) {
                                    disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getOpponent(cache.getTrainer()._id())._id(),
                                                    fightingMonsters.get(0)._id(), new ChangeMonsterMove("change-monster", fightingMonsters.get(0)._id()))
                                            .observeOn(FX_SCHEDULER)
                                            .subscribe(update -> {
                                                encounterController.setOwnMonster(fightingMonsters.get(0));
                                                encounterController.setToUpdateUIOnChange();
                                                app.show(encounterController);
                                            }, Throwable::printStackTrace));
                                } else {
                                    disposables.add(encounterOpponentsService.updateEncounterOpponent(cache.getJoinedRegion()._id(), cache.getCurrentEncounter()._id(), cache.getOpponent(cache.getTrainer()._id())._id(),
                                                    null, new ChangeMonsterMove("change-monster", fightingMonsters.get(0)._id()))
                                            .observeOn(FX_SCHEDULER)
                                            .subscribe(update -> {
                                                encounterController.setOwnMonster(fightingMonsters.get(0));
                                                encounterController.setToUpdateUIOnChange();
                                                app.show(encounterController);
                                            }, Throwable::printStackTrace));
                                }
                            }
                    ));
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
