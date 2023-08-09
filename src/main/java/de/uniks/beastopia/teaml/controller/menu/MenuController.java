package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.service.*;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MenuController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    @FXML
    public VBox left;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button logoutBtn;
    @FXML
    public Button editProfileBtn;
    @FXML
    public ImageView editProfileImage;
    @FXML
    public ImageView settingsImage;
    @Inject
    Provider<RegionController> regionControllerProvider;
    @Inject
    Provider<FriendListController> friendListControllerProvider;
    @Inject
    Provider<LoginController> loginControllerProvider;
    @Inject
    Provider<EditProfileController> editProfileControllerProvider;
    @Inject
    Provider<SettingsController> settingsControllerProvider;
    @Inject
    AuthService authService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    ImageService imageService;
    @Inject
    AchievementsService achievementsService;
    @Inject
    DataCache cache;
    @Inject
    Prefs prefs;
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
    public MenuController() {

    }

    @Override
    public void init() {
        super.init();
        app.addCleanupTask(() -> authService.goOffline().subscribe());

        if (cache.getTrainer() != null) {
            cache.setTrainer(null);
        }

        // Reset current achievements and load achievements to user account
        if (cache.getMyAchievements().isEmpty()) {
            disposables.add(achievementsService.getUserAchievements(tokenStorage.getCurrentUser()._id())
                    .subscribe(achievements -> cache.setMyAchievements(achievements)));
        }

        if (prefs.getVisitedAreas() != null) {
            String storedAreasString = prefs.getVisitedAreas();
            String[] storedAreas = storedAreasString.split(";");

            for (String id : storedAreas) {
                cache.addVisitedArea(id);
            }
        }
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        banner.setImage(imageService.getBanner());
        userAvatar.setImage(cache.getImageAvatar(tokenStorage.getCurrentUser()));
        userName.setText(tokenStorage.getCurrentUser().name());
        editProfileImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/edit.png")))));
        settingsImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/settings.png")))));

        Controller friendListController = friendListControllerProvider.get();
        subControllers.add(friendListController);
        Parent friendListParent = friendListController.render();
        friendListContainer.getChildren().add(friendListParent);
        VBox.setVgrow(friendListParent, javafx.scene.layout.Priority.ALWAYS);

        Controller regionController = regionControllerProvider.get();
        subControllers.add(regionController);
        Parent regionParent = regionController.render();
        regionContainer.getChildren().add(regionParent);
        VBox.setVgrow(regionParent, javafx.scene.layout.Priority.ALWAYS);
        return parent;
    }

    @Override
    public void destroy() {
        subControllers.forEach(Controller::destroy);
        banner = null;
        userAvatar = null;
        super.destroy();
    }

    @Override
    public String getTitle() {
        return resources.getString("titleMenu");
    }

    @FXML
    public void logout() {
        disposables.add(authService.logout().observeOn(FX_SCHEDULER).subscribe(
                lr -> {
                    app.show(loginControllerProvider.get());
                    cache.setMyAchievements(Collections.emptyList());
                },
                error -> Dialog.error(error, "Logout failed")
        ));
    }

    @FXML
    public void editProfileButtonPressed() {
        app.show(editProfileControllerProvider.get());
    }

    public void settingsButtonPressed() {
        app.show(settingsControllerProvider.get());
    }
}
