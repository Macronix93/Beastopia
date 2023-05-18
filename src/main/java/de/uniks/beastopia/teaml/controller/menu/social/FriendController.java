package de.uniks.beastopia.teaml.controller.menu.social;


import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.ws.EventListener;
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

public class FriendController extends Controller {
    @FXML
    ImageView friendAvatar;
    @FXML
    Circle statusCircle;
    @FXML
    Text name;
    @FXML
    Button addRemoveFriendButton;
    @FXML
    Button chat;
    @FXML
    Button pin;
    @FXML
    HBox _rootElement;

    private User user;

    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;

    private Boolean friendPin;

    private ImageView pinned;
    private ImageView notPinned;
    private ImageView addImage;
    private ImageView removeImage;
    @Inject
    Preferences preferences;
    @Inject
    FriendListService friendListService;
    @Inject
    EventListener eventListener;

    private Consumer<User> onFriendChanged = null;
    private Consumer<User> onPinChanged = null;

    @Inject
    public FriendController() {

    }

    @Override
    public void init() {
        disposables.add(eventListener.listen("users." + user._id() + ".updated", User.class)
                .observeOn(FX_SCHEDULER)
                .subscribe(event -> {
                    final User user = event.data();
                    updateOnlineStatus(user);
                }));

        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")));
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")));
            addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")));
            removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")));
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnFriendChanged(Consumer<User> onFriendChanged) {
        this.onFriendChanged = onFriendChanged;
    }

    public void setOnPinChanged(Consumer<User> onPinChanged) {
        this.onPinChanged = onPinChanged;
    }

    public FriendController setUser(User user, boolean friendPin) {
        this.user = user;
        this.friendPin = friendPin;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        //TODO change avatar URL when avatar upload is implemented to individual link
        try {
            Image image = loadImage(Objects.requireNonNull(Main.class.getResource("assets/Lumnix_Logo_tr.png")),
                    40.0, 40.0, false, false);
            friendAvatar.setImage(image);
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        name.setText(user.name());
        updateOnlineStatus(user);

        if (this.friendPin) {
            this.pin.setGraphic(pinned);
        } else {
            this.pin.setGraphic(notPinned);
        }

        if (friendListService.isFriend(user)) {
            addRemoveFriendButton.setGraphic(removeImage);
        } else {
            addRemoveFriendButton.setGraphic(addImage);
        }

        return parent;
    }

    private void updateOnlineStatus(User user) {
        if (user.status().equals(STATUS_ONLINE)) {
            statusCircle.setFill(Paint.valueOf("green"));
        } else {
            statusCircle.setFill(Paint.valueOf("red"));
        }
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
    public void addRemoveFriend() {
        if (friendListService.isFriend(user)) {
            disposables.add(friendListService.removeFriend(user)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(user -> {
                        if (onFriendChanged != null) {
                            onFriendChanged.accept(user);
                        }
                    }));
        } else {
            disposables.add(friendListService.addFriend(user)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(user -> {
                        if (onFriendChanged != null) {
                            onFriendChanged.accept(user);
                        }
                    }));
        }
    }

    @FXML
    public void openFriendChat() {
        app.show(directMessageControllerProvider.get().setupDirectMessageController("global", user._id()));
    }

    @FXML
    public void pinFriend() {
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
