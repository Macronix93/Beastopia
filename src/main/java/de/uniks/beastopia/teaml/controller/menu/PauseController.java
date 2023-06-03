package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.controller.ingame.TrainerController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class PauseController extends Controller {

    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public Button editProfileButton;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    Provider<EditProfileController> editProfileControllerProvider;
    @Inject
    Provider<SettingsController> settingsControllerProvider;
    @FXML
    private VBox friendListContainer;
    @Inject
    Provider<TrainerController> trainerControllerProvider;

    private Trainer trainer;
    private Region region;

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
        app.show(editProfileControllerProvider.get().backController("pause"));
    }

    @FXML
    public void settingsButtonPressed() {
        app.show(settingsControllerProvider.get().backController("pause"));
    }

    @FXML
    public void mainMenuButtonPressed() {
        app.show(menuControllerProvider.get());
    }

    @FXML
    public void trainerMenuButtonPressed() {
        TrainerController controller = trainerControllerProvider.get();
        controller.setRegion(region);
        controller.backController("pause");
        app.show(controller);
    }

    @FXML
    public void pauseMenu(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            IngameController controller = ingameControllerProvider.get();
            controller.setRegion(region);
            app.show(controller);
        }
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
