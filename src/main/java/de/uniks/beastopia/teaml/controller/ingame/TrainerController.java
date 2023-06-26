package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.AchievementsService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Date;
import java.util.stream.IntStream;

public class TrainerController extends Controller {
    public static final int PREVIEW_SCALING = 3;
    public static final Rectangle2D PREVIEW_VIEWPORT = new javafx.geometry.Rectangle2D(48 * PREVIEW_SCALING, 0, 16 * PREVIEW_SCALING, 32 * PREVIEW_SCALING);

    @SuppressWarnings("unused")
    @FXML
    private VBox trainerContainer;
    @FXML
    private TextField trainerNameInput;
    @FXML
    private Text regionNameDisplay;
    @FXML
    private ImageView trainerSprite;
    @FXML
    private Button deleteTrainerButton;
    @FXML
    private Text spriteNameDisplay;
    @FXML
    private Button saveTrainerButton;

    @Inject
    TokenStorage tokenStorage;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    AchievementsService achievementsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<DeleteTrainerController> deleteTrainerControllerProvider;

    private Trainer trainer;
    private Region region;
    private String backController;
    private LoadingPage loadingPage;
    private String currentSprite = "";

    private final SimpleStringProperty trainerName = new SimpleStringProperty();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    @Inject
    public TrainerController() {
    }

    @Override
    public void init() {
        super.init();

        this.trainer = cache.getTrainer();
    }

    public void saveTrainer() {
        String nameInput = trainerNameInput.getText();
        String trainerImage = currentSprite;

        if (nameInput == null || nameInput.isEmpty()) {
            Dialog.error(resources.getString("trainerNameMissing"), resources.getString("enterTrainerName"));
            return;
        }

        // Either change the current trainer or create
        if (cache.getTrainer() == null) {
            disposables.add(trainerService.createTrainer(region._id(), nameInput, trainerImage)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(tr -> {
                        checkTrainerAchievement();

                        cache.setTrainer(tr);
                        showIngameController(region);
                    }, error -> Dialog.error(error, "Trainer creation failed!")));
        } else {
            disposables.add(trainerService.updateTrainer(region._id(), cache.getTrainer()._id(), nameInput, trainerImage)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(tr -> {
                        cache.setTrainer(tr);
                        showIngameController(region);
                    }, error -> Dialog.error(error, "Trainer adjustments failed!")));
        }
    }

    public void deleteTrainer() {
        DeleteTrainerController deleteTrainerController = deleteTrainerControllerProvider.get();
        deleteTrainerController.setRegion(region);
        app.show(deleteTrainerController);
    }

    public void back() {
        if (this.backController.equals("menu")) {
            app.show(menuControllerProvider.get());
        } else {
            showIngameController(region);
        }
    }

    public void showIngameController(Region region) {
        checkRegionAchievement();

        IngameController ingameController = ingameControllerProvider.get();
        ingameController.setRegion(region);
        app.show(ingameController);
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        trainerNameInput.textProperty().bindBidirectional(trainerName);
        regionNameDisplay.setText(region.name());

        if (trainer == null) {
            // Disable trainer deletion button if no trainer present
            deleteTrainerButton.setDisable(true);

            // Check if current user has a trainer for the specified region
            disposables.add(trainerService.getAllTrainer(region._id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(trainers -> trainers.stream()
                                    .filter(t -> t.user().equals(tokenStorage.getCurrentUser()._id()))
                                    .findFirst()
                                    .ifPresentOrElse(tr -> {
                                        checkTrainerAchievement();

                                        cache.setTrainer(tr);
                                        showIngameController(region);
                                    }, this::loadCharacterSelection),
                            error -> Dialog.error(error, "Trainer loading failed!")));
        } else {
            loadCharacterSelection();
        }

        return loadingPage.parent();
    }

    private void loadCharacterSelection() {
        disposables.add(presetsService.getCharacters()
                .observeOn(FX_SCHEDULER)
                .subscribe(characters -> {
                    cache.setCharacters(characters);

                    showTrainers();

                    disposables.add(delay().subscribe(t -> {
                        for (Pair<String, Image> pair : cache.getCharacters()) {
                            if (cache.getCharacterImage(pair.getKey()).getValue() == null) {
                                cache.setCharacterImage(pair.getKey(), new Image("https://stpmon.uniks.de/api/v3/presets/characters/" + pair.getKey(), 383 * PREVIEW_SCALING, 96 * PREVIEW_SCALING, true, false));
                                onUI(this::updateImages);
                            }
                        }
                    }));
                    loadingPage.setDone();
                }));
    }

    private void showTrainers() {
        if (trainer == null) {
            showTrainerSpritePreview(cache.getCharacters().get(0).getKey(), cache.getCharacters().get(0).getValue());
        } else {
            showTrainerSpritePreview(cache.getCharacterImage(trainer.image()).getKey(), cache.getCharacterImage(trainer.image()).getValue());

            // Find index of the found trainer
            currentIndex.set(IntStream.range(0, cache.getCharacters().size())
                    .filter(i -> cache.getCharacters().get(i).getKey().equals(trainer.image()))
                    .findFirst()
                    .orElse(-1));

            trainerNameInput.setText(trainer.name());
            trainerNameInput.positionCaret(trainer.name().length());
        }
    }

    private void updateImages() {
        Pair<String, Image> pair = cache.getCharacterImage(currentSprite);
        showTrainerSpritePreview(pair.getKey(), pair.getValue());
    }

    @Override
    public String getTitle() {
        return resources.getString("titleTrainer");
    }

    @FXML
    public void chooseLeft() {
        currentIndex.set(currentIndex.get() - 1);

        if (currentIndex.get() < 0) {
            currentIndex.set(cache.getCharacters().size() - 1);
        }

        showTrainerSpritePreview(
                cache.getCharacters().get(currentIndex.get()).getKey(),
                cache.getCharacters().get(currentIndex.get()).getValue()
        );
    }

    @FXML
    public void chooseRight() {
        currentIndex.set(currentIndex.get() + 1);

        if (currentIndex.get() >= cache.getCharacters().size()) {
            currentIndex.set(0);
        }

        showTrainerSpritePreview(
                cache.getCharacters().get(currentIndex.get()).getKey(),
                cache.getCharacters().get(currentIndex.get()).getValue()
        );
    }

    @SuppressWarnings("UnusedReturnValue")
    public TrainerController backController(String controller) {
        this.backController = controller;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void showTrainerSpritePreview(String charName, Image sprite) {
        currentSprite = charName;
        saveTrainerButton.setDisable(sprite == null);
        deleteTrainerButton.setDisable(sprite == null || trainer == null);
        trainerSprite.setFitWidth(16 * PREVIEW_SCALING);
        trainerSprite.setFitHeight(32 * PREVIEW_SCALING);
        trainerSprite.setImage(sprite);
        trainerSprite.setViewport(PREVIEW_VIEWPORT);
        trainerSprite.setSmooth(false);

        spriteNameDisplay.setText(stripString(charName));
    }

    public String stripString(String stringToStrip) {
        stringToStrip = stringToStrip
                .replace("_", " ")
                .replace("16x16", "");
        stringToStrip = stringToStrip.substring(0, stringToStrip.lastIndexOf("."));
        return stringToStrip;
    }

    private void checkTrainerAchievement() {
        Achievement firstTrainerAchievement = null;

        for (Achievement achievement : cache.getMyAchievements()) {
            if (achievement.id().equals("FirstTrainer")) {
                firstTrainerAchievement = achievement;
                break;
            }
        }

        if (firstTrainerAchievement == null) {
            Date date = new Date();

            disposables.add(achievementsService.updateUserAchievement(tokenStorage.getCurrentUser()._id(), "FirstTrainer", new Achievement(null, null, "FirstTrainer", tokenStorage.getCurrentUser()._id(), date, 100))
                    .subscribe(a -> cache.addMyAchievement(a)));
        }
    }

    private void checkRegionAchievement() {
        Achievement firstRegionAchievement = null;

        for (Achievement achievement : cache.getMyAchievements()) {
            if (achievement.id().equals("FirstRegion")) {
                firstRegionAchievement = achievement;
                break;
            }
        }

        if (firstRegionAchievement == null) {
            Date date = new Date();

            disposables.add(achievementsService.updateUserAchievement(tokenStorage.getCurrentUser()._id(), "FirstRegion", new Achievement(null, null, "FirstRegion", tokenStorage.getCurrentUser()._id(), date, 100))
                    .subscribe(a -> cache.addMyAchievement(a)));
        }
    }
}
