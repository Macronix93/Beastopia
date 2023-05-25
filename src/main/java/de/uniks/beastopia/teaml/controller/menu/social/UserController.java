package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
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

    private boolean pin;
    private User user;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView add;
    private ImageView remove;

    @Inject
    public UserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        username.setText(user.name());

        if (pin) {
            this.pinButton.setGraphic(pinned);
        } else {
            this.pinButton.setGraphic(notPinned);
        }

        if (createGroupControllerProvider.get().getAddedUsersList().contains(user)) {
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

    public void setUser(User user, boolean pin) {
        this.user = user;
        this.pin = pin;
    }


    @FXML
    public void addRemove() {
        if (createGroupControllerProvider.get().getAddedUsersList().contains(user)) {
            createGroupControllerProvider.get().addUser(this.user);
        } else {
            createGroupControllerProvider.get().removeUser(this.user);
        }
    }

    @FXML
    public void pin() {

    }

    private ImageView createImage(String imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitHeight(20.0);
        imageView.setFitWidth(20.0);
        return imageView;
    }
}
