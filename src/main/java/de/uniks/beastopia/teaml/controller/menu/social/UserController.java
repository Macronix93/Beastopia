package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.utils.AssetProvider;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.function.Consumer;


public class UserController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label username;
    @FXML
    public Button addRemoveButton;
    @FXML
    public Button pinButton;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;
    @Inject
    ImageService imageService;
    @Inject
    AssetProvider assets;
    private User user;
    private Consumer<User> onUserToggled;
    private Consumer<User> onUserPinToggled;
    private boolean isAdded;

    @Inject
    public UserController() {

    }

    public UserController setOnUserToggled(Consumer<User> onUserToggled) {
        this.onUserToggled = onUserToggled;
        return this;
    }

    public UserController setOnUserPinToggled(Consumer<User> onUserPinToggled) {
        this.onUserPinToggled = onUserPinToggled;
        return this;
    }

    public UserController setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        username.setText(user.name());
        avatar.setImage(cache.getImageAvatar(user));

        if (prefs.isPinned(user)) {
            ImageView pin = imageService.getPinnedImage();
            pin.setFitHeight(15);
            pin.setFitWidth(15);
            this.pinButton.setGraphic(pin);
        } else {
            ImageView pin = imageService.getNotPinnedImage();
            pin.setFitHeight(15);
            pin.setFitWidth(15);
            this.pinButton.setGraphic(pin);
        }

        if (isAdded) {
            this.addRemoveButton.setGraphic(assets.getIcon("buttons", "minus", 15, 15));
        } else {
            this.addRemoveButton.setGraphic(assets.getIcon("buttons", "plus", 15, 15));
        }

        return parent;
    }


    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void addRemove() {
        onUserToggled.accept(user);
    }

    @FXML
    public void pin() {
        onUserPinToggled.accept(user);
    }

    @Override
    public void destroy() {
        avatar = null;
        super.destroy();
    }
}