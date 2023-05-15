package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;

public class ChatUserController extends Controller {

    @FXML
    ImageView userAvatar;
    @FXML
    Circle statusCircle;
    @FXML
    Text name;
    @FXML
    Button removeChatButton;
    @FXML
    Button pin;
    @FXML
    HBox _rootElement;

    private User user;

    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;

    private Boolean userPin;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView abort;

    @Inject
    Preferences preferences;

    private Consumer<User> onPinChanged = null;

    @Inject
    public ChatUserController() {

    }


    @Override
    public void init() {
        User user = new User(null, null, null, "name", "status", null, null);
        this.user = user;
        this.userPin = true;

        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")));
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")));
            abort = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/abort.png")));
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatUserController setUser(User user, boolean userPin) {
        this.user = user;
        this.userPin = userPin;
        return this;
    }

    public void setOnPinChanged(Consumer<User> onPinChanged) {
        this.onPinChanged = onPinChanged;
    }


    @Override
    public Parent render() {
        Parent parent = super.render();

        //TODO change avatar URL when avatar upload is implemented to individual link
        try {
            Image image = loadImage(Objects.requireNonNull(Main.class.getResource("assets/Lumnix_Logo_tr.png")),
                    40.0, 40.0, false, false);
            userAvatar.setImage(image);
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        name.setText(user.name());
        removeChatButton.setGraphic(abort);

        if (user.status().equals(STATUS_ONLINE)) {
            statusCircle.setFill(Paint.valueOf("green"));
        } else {
            statusCircle.setFill(Paint.valueOf("red"));
        }

        if (this.userPin) {
            this.pin.setGraphic(pinned);
        } else {
            this.pin.setGraphic(notPinned);
        }

        return parent;
    }

    private ImageView createImage(URL imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(loadImage(imageUrl));
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }

    private static Image loadImage(URL imageUrl) throws FileNotFoundException, URISyntaxException {
        return new Image(new FileInputStream(new File(imageUrl.toURI())));
    }

    @SuppressWarnings("SameParameterValue")
    private static Image loadImage(URL imageUrl, double width, double height, boolean preserveRatio, boolean smooth) throws FileNotFoundException, URISyntaxException {
        return new Image(new FileInputStream(new File(imageUrl.toURI())), width, height, preserveRatio, smooth);
    }

    @FXML
    public void pinUser() {
        if (pin.getGraphic() == notPinned) {
            pin.setGraphic(pinned);
            preferences.putBoolean(this.user._id() + "_pinned", true);
        } else {
            pin.setGraphic(notPinned);
            preferences.putBoolean(this.user._id() + "_pinned", false);
        }
        if (onPinChanged != null) {
            onPinChanged.accept(user);
        }
    }

}
