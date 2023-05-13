package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class PauseController extends Controller {

    @FXML
    private VBox friendListContainer;

    private final List<Controller> subControllers = new ArrayList<>();
    @Inject
    App app;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    Provider<MenuController> menuControllerProvider;

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
        return "Beastopia - Pause Menu";
    }

    public void editProfileButtonPressed() {
    }

    public void mainMenuButtonPressed() {
        app.show(menuControllerProvider.get());
    }
}
