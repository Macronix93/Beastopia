package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.sockets.EventListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Objects;


public class UserController extends Controller {

    @FXML
    public ImageView avatar;
    @FXML
    public Label username;
    private boolean pin;
    private User user;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView addImage;
    private ImageView removeImage;

    @Inject
    public UserController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        username.setText(user.name());

        if (pin) {
            this.pinned.setImage(pinned.getImage());
        } else {
            this.pinned.setImage(notPinned.getImage());
        }


        return parent;
    }

    @Override
    public void init() {
        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")).toString());
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")).toString());
            addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());
            removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
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
