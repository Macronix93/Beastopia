package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
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
    public VBox left;
    @Inject
    Provider<RegionController> regionControllerProvider;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    Provider<LoginController> loginControllerProvider;
    @Inject
    Provider<EditProfileController> editProfileControllerProvider;
    @Inject
    AuthService authService;
    @FXML
    public Button logoutBtn;
    @FXML
    public Button editProfileBtn;

    @FXML
    private VBox friendListContainer;
    @FXML
    private VBox regionContainer;
    @SuppressWarnings("unused")
    @FXML
    private ImageView banner;
    @SuppressWarnings("unused")
    @FXML
    private ImageView userAvatar;
    @FXML
    private Text userName;

    @Inject
    TokenStorage tokenStorage;
    @Inject
    DataCache cache;

    @Inject
    public MenuController() {

    }

    @Override
    public void init() {
        super.init();
        app.addCleanupTask(() -> authService.goOffline().subscribe());

        cache.setTrainer(null);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        //set beastopia banner

        //set userAvatar

        userName.setText(tokenStorage.getCurrentUser().name());

        Controller friendListController = friendListControllerProvider.get();
        subControllers.add(friendListController);
        Parent render = friendListController.render();
        friendListContainer.getChildren().add(render);
        VBox.setVgrow(render, javafx.scene.layout.Priority.ALWAYS);

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
        return resources.getString("titleMenu");
    }

    @FXML
    public void logout() {
        disposables.add(authService.logout().observeOn(FX_SCHEDULER).subscribe(
                lr -> app.show(loginControllerProvider.get()),
                error -> Dialog.error(error, "Logout failed")));
    }

    @FXML
    public void editProfileButtonPressed() {
        app.show(editProfileControllerProvider.get().backController("menu"));
    }
}
