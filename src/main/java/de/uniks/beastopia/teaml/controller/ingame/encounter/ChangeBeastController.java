package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChangeBeastController extends Controller {
    @FXML
    public VBox currentBeasts;
    @FXML
    public VBox beastTeam;
    public ImageView removeImage;
    public ImageView addImage;

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

    public ChangeBeastController setCurrentMonster(Monster currentMonster) {
        this.currentMonster = currentMonster;
        return this;
    }

    public ChangeBeastController setEncounterController(EncounterController controller) {
        this.encounterController = controller;
        return this;
    }

    @Override
    public void init() {
        super.init();

        removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
        addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());
    }

    @Override
    public String getTitle() {
        return resources.getString("titleChangeBeasts");
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        disposables.add(trainerService.getTrainerMonsters(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    playerMonsters.addAll(monsters);

                    for (Monster monster : playerMonsters) {
                        ChangeBeastElementController controller = changeBeastElementControllerProvider.get()
                                .setMonster(monster)
                                .setParentController(this);

                        if (monster._id().equals(currentMonster._id())) {
                            currentBeasts.getChildren().add(controller.render());
                            controller.addOrRemoveButton.setGraphic(removeImage);

                            fightingMonsters.add(monster);
                        } else {
                            beastTeam.getChildren().add(controller.render());

                            bankMonsters.add(monster);
                        }
                    }

                    System.out.println("Fighting Monsters: " + fightingMonsters + " | Bank Monsters: " + bankMonsters);

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
        System.out.println("current monster: " + currentMonster._id());
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
