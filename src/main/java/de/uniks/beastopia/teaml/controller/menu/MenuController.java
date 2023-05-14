package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class MenuController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public Button logoutBtn;
    @FXML
    public Button editProfileBtn;

    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;
    @FXML
    private ImageView banner;
    @FXML
    private ImageView userAvatar;
    @FXML
    private Text userName;

    @Inject
    Provider<RegionController> regionControllerProvider;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    public MenuController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        //set beastopia banner
        //set userAvatar

        userName.setText(tokenStorage.getCurrentUser().name());

        Controller friendListController = friendListControllerProvider.get();
        subControllers.add(friendListController);
        friendListContainer.getChildren().add(friendListController.render());
        Controller regionController = regionControllerProvider.get();
        subControllers.add(regionController);
        regionContainer.getChildren().add(regionController.render());
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        super.destroy();
    }

    @Override
    public String getTitle() {
        return "Beastopia - Main Menu";
    }


    @FXML
    public void logout() {
    }

    @FXML
    public void editProfileButtonPressed() {
        //app.show(editProfileControllerProvider.get());
    }
}
