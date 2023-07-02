package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.TrainerController;
import de.uniks.beastopia.teaml.controller.ingame.beast.EditBeastTeamController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.utils.SoundController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class PauseController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();
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
    public void pauseMenu() {
        onCloseRequest.run();
    }

    public void beastTeamButtonPressed() {
        EditBeastTeamController beastTeam = editBeastTeamControllerProvider.get();
        app.show(beastTeam);
    }
}
