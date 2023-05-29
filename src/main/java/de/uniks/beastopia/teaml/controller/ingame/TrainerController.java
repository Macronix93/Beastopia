package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

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

    @Inject
    TokenStorage tokenStorage;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;

    private Region region;
    private Trainer trainer;
    private String trainerSpriteSheet;
    private List<String> characterImageStrings = new ArrayList<>();

    private final SimpleStringProperty trainerName = new SimpleStringProperty();
    private final IntegerProperty currentIndex = new SimpleIntegerProperty(0);

    @Inject
    public TrainerController() {
    }

    @Override
    public void init() {
        super.init();

        // Check if current user has a trainer for the specified region
        disposables.add(trainerService.getAllTrainer(region._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(trainers -> {
                    Trainer tr = trainers.stream().filter(t -> t.user().equals(tokenStorage.getCurrentUser()._id())).findFirst().orElse(null);

                    if (tr != null) {
                        setTrainer(tr);

                        app.show(ingameControllerProvider.get());
                    }
                }));

        // Add currently available character sprite strings to list
        disposables.add(presetsService.getCharacters()
                .observeOn(FX_SCHEDULER)
                .subscribe(characters -> {
                    if (characters != null) {
                        characterImageStrings.addAll(characters);

                        trainerSprite.setImage(presetsService.getCharacterSprites(characterImageStrings.get(0)).blockingFirst());
                        trainerSprite.setPreserveRatio(true);
                        trainerSprite.setSmooth(true);
                        trainerSprite.setViewport(new javafx.geometry.Rectangle2D(48, 0, 16, 32));
                    }
                }));
    }

    public void saveTrainer() {
        if (trainerNameInput.getText() == null || trainerNameInput.getText().isEmpty()) {
            Dialog.error(resources.getString("trainerNameMissing"), resources.getString("enterTrainerName"));
            return;
        }

        disposables.add(trainerService.createTrainer(region._id(), trainerNameInput.getText(), characterImageStrings.get(currentIndex.get()))
                .observeOn(FX_SCHEDULER).subscribe(tr -> {
                    setTrainer(tr);
                    setTrainerSpriteSheet(characterImageStrings.get(currentIndex.get()));

                    app.show(ingameControllerProvider.get());
                }, error -> Dialog.error(error, "Trainer creation failed!")));
    }

    public void deleteTrainer() {
        //TODO: Delete trainer for current region
    }

    public void back() {
        app.show(menuControllerProvider.get());
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        trainerNameInput.textProperty().bindBidirectional(trainerName);
        regionNameDisplay.setText("RegionName");
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleTrainer");
    }

    public void chooseLeft() {
        currentIndex.set(currentIndex.get() - 1);

        if (currentIndex.get() < 0) {
            currentIndex.set(characterImageStrings.size() - 1);
        }

        trainerSprite.setImage(presetsService.getCharacterSprites(characterImageStrings.get(currentIndex.get())).blockingFirst());
    }

    public void chooseRight() {
        currentIndex.set(currentIndex.get() + 1);

        if (currentIndex.get() >= characterImageStrings.size()) {
            currentIndex.set(0);
        }

        trainerSprite.setImage(presetsService.getCharacterSprites(characterImageStrings.get(currentIndex.get())).blockingFirst());
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return this.trainer;
    }

    public void setTrainerSpriteSheet(String trainerSpriteSheet) {
        this.trainerSpriteSheet = trainerSpriteSheet;
    }

    public String getTrainerSpriteSheet() {
        return this.trainerSpriteSheet;
    }
}
