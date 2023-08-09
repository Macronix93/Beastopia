package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.TrainerController;
import de.uniks.beastopia.teaml.controller.ingame.beast.EditBeastTeamController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.utils.SoundController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PauseController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public ImageView settingsImage;
    @FXML
    public ImageView editProfileImage;
    @FXML
    public ImageView backToMainImage;
    @FXML
    public ImageView trainerMenuImage;
    @FXML
    public ImageView beastTeamImage;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<EditProfileController> editProfileControllerProvider;
    @Inject
    Provider<SettingsController> settingsControllerProvider;
    @Inject
    Provider<TrainerController> trainerControllerProvider;
    @Inject
    Provider<EditBeastTeamController> editBeastTeamControllerProvider;
    @Inject
    Provider<SoundController> soundControllerProvider;
    @Inject
    ImageService imageService;

    @FXML
    private VBox friendListContainer;
    private Runnable onCloseRequest;

    @Inject
    public PauseController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        Controller subController = friendListControllerProvider.get();
        subControllers.add(subController);
        friendListContainer.getChildren().add(subController.render());

        settingsImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/settings.png")))));
        editProfileImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/edit.png")))));
        backToMainImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/back.png")))));
        trainerMenuImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/trainer.png")))));
        beastTeamImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/beast.png")))));

        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
    }

    @Override
    public String getTitle() {
        return resources.getString("titlePause");
    }

    @FXML
    public void editProfileButtonPressed() {
        app.show(editProfileControllerProvider.get());
    }

    @FXML
    public void settingsButtonPressed() {
        app.show(settingsControllerProvider.get());
    }

    @FXML
    public void mainMenuButtonPressed() {
        if (soundControllerProvider.get().getBgmPlayer() != null) {
            soundControllerProvider.get().stopBGM();
        }
        app.show(menuControllerProvider.get());
    }

    @FXML
    public void trainerMenuButtonPressed() {
        if (soundControllerProvider.get().getBgmPlayer() != null) {
            soundControllerProvider.get().stopBGM();
        }

        TrainerController controller = trainerControllerProvider.get();
        controller.backController("pause");
        app.show(controller);
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    @FXML
    public void pauseMenu(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            onCloseRequest.run();
        }
    }

    public void beastTeamButtonPressed() {
        EditBeastTeamController beastTeam = editBeastTeamControllerProvider.get();
        app.show(beastTeam);
    }
}
