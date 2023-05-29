package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Objects;
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
    Provider<CreateGroupController> createGroupControllerProvider;
    @Inject
    Prefs prefs;

    private User user;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView add;
    private ImageView remove;
    private Consumer<User> onUserToggled;
    private Consumer<User> onUserPinToggled;
    private boolean isAdded;

    @Inject
    public UserController() {

    }

    @SuppressWarnings("UnusedReturnValue")
    public UserController setOnUserToggled(Consumer<User> onUserToggled) {
        this.onUserToggled = onUserToggled;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
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

        if (prefs.isPinned(user)) {
            this.pinButton.setGraphic(pinned);
        } else {
            this.pinButton.setGraphic(notPinned);
        }

        if (isAdded) {
            this.addRemoveButton.setGraphic(remove);
        } else {
            this.addRemoveButton.setGraphic(add);
        }

        return parent;
    }

    @Override
    public void init() {
        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")).toString());
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")).toString());
            add = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());
            remove = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    private ImageView createImage(String imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitHeight(20.0);
        imageView.setFitWidth(20.0);
        return imageView;
    }
}