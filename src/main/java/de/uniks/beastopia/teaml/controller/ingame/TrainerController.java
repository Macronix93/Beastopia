package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TrainerController extends Controller {
    @FXML
    private VBox trainerContainer;
    @FXML
    private TextField trainerNameInput;
    @FXML
    private Text regionNameDisplay;
    @FXML
    private ImageView trainerSprite;
    @FXML
    private Button backButton;
    @FXML
    private Button chooseLeftButton;
    @FXML
    private Button chooseRightButton;
    @FXML
    private Button deleteTrainerButton;
    @FXML
    private Button saveTrainerButton;
    @FXML
    private Text spriteNameDisplay;

    @Inject
    Prefs prefs;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
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
    private static final int PREVIEW_SCALING = 3;

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
        String trainerImage = cache.getCharacters().get(currentIndex.get()).getKey();

        if (nameInput == null || nameInput.isEmpty()) {
            Dialog.error(resources.getString("trainerNameMissing"), resources.getString("enterTrainerName"));
            return;
        }

        // Either change the current trainer or create
        if (cache.getTrainer() == null) {
            disposables.add(trainerService.createTrainer(region._id(), nameInput, trainerImage)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(tr -> showIngameController(region), error -> Dialog.error(error, "Trainer creation failed!")));
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
        app.show(deleteTrainerControllerProvider.get());
    }

    public void back() {
        if (this.backController.equals("menu")) {
            app.show(menuControllerProvider.get());
        } else {
            showIngameController(region);
        }
    }

    public void showIngameController(Region region) {
        IngameController ingameController = ingameControllerProvider.get();
        ingameController.setRegion(region);
        app.show(ingameController);
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        trainerNameInput.textProperty().bindBidirectional(trainerName);
        regionNameDisplay.setText(region.name());

        // Check if list of character strings and images is empty
        if (cache.getCharacters().isEmpty()) {
            // Add currently available character sprite strings to Pair list in cache
            disposables.add(presetsService.getCharacters()
                    .observeOn(FX_SCHEDULER)
                    .subscribe(characters -> {
                        if (characters != null) {
                            List<Pair<String, Image>> charList = new ArrayList<>();
                            for (String charImg : characters) {
                                charList.add(new Pair<>(charImg, new Image("https://stpmon.uniks.de/api/v2/presets/characters/" + charImg)));
                            }
                            cache.setCharacters(charList);

                            showTrainerSpritePreview(cache.getCharacters().get(0).getKey(), cache.getCharacters().get(0).getValue());
                            checkForExistingTrainer();

                            loadingPage.setDone();
                        }
                    }));
        } else {
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

            checkForExistingTrainer();

            loadingPage.setDone();
        }

        return loadingPage.parent();
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

        trainerSprite.setImage(cache.getCharacters().get(currentIndex.get()).getValue());
        spriteNameDisplay.setText(stripString(cache.getCharacters().get(currentIndex.get()).getKey()));
    }

    @FXML
    public void chooseRight() {
        currentIndex.set(currentIndex.get() + 1);

        if (currentIndex.get() >= cache.getCharacters().size()) {
            currentIndex.set(0);
        }

        trainerSprite.setImage(cache.getCharacters().get(currentIndex.get()).getValue());
        spriteNameDisplay.setText(stripString(cache.getCharacters().get(currentIndex.get()).getKey()));
    }

    @SuppressWarnings("UnusedReturnValue")
    public TrainerController backController(String controller) {
        this.backController = controller;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void checkForExistingTrainer() {
        if (trainer == null) {
            // Check if current user has a trainer for the specified region
            disposables.add(trainerService.getAllTrainer(region._id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(trainers -> trainers.stream()
                            .filter(t -> t.user().equals(tokenStorage.getCurrentUser()._id()))
                            .findFirst()
                            .ifPresent(tr -> {
                                cache.setTrainer(tr);
                                showIngameController(region);
                            })));
        }
    }

    public void showTrainerSpritePreview(String charName, Image sprite) {
        trainerSprite.setImage(sprite);
        trainerSprite.setPreserveRatio(true);
        trainerSprite.setViewport(new javafx.geometry.Rectangle2D(48, 0, 16, 32));
        trainerSprite.setFitWidth(32 * PREVIEW_SCALING);
        trainerSprite.setFitHeight(32 * PREVIEW_SCALING);
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
}
