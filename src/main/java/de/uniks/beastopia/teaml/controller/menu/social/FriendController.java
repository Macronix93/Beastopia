package de.uniks.beastopia.teaml.controller.menu.social;


import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Prefs;
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
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.function.Consumer;

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
    FriendListService friendListService;
    @Inject
    Prefs prefs;
    @Inject
    EventListener eventListener;

    private Consumer<User> onFriendChanged = null;
    private Consumer<User> onPinChanged = null;
    private boolean friend;

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
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")).toString());
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")).toString());
            addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());
            removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnFriendChanged(Consumer<User> onFriendChanged) {
        this.onFriendChanged = onFriendChanged;
    }

    public void checkFriend(boolean friend) {
        this.friend = friend;
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
            Image image = loadImage(Objects.requireNonNull(Main.class.getResource("assets/Lumnix_Logo_tr.png")).toString(),
                    40.0, 40.0, false, false);
            friendAvatar.setImage(image);
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        name.setText(user.name());
        updateOnlineStatus(user);

        if (friend) {
            if (this.friendPin) {
                this.pin.setGraphic(pinned);
            } else {
                this.pin.setGraphic(notPinned);
            }
        } else {
            this.pin.setGraphic(notPinned); //that buttons align
            this.pin.setVisible(false);
            this.pin.setDisable(true);
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

    private ImageView createImage(String imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }

    @SuppressWarnings("SameParameterValue")
    private static Image loadImage(String imageUrl, double width, double height, boolean preserveRatio, boolean smooth) throws FileNotFoundException, URISyntaxException {
        return new Image(imageUrl, width, height, preserveRatio, smooth);
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
        app.show(directMessageControllerProvider.get().setupDirectMessageController(user));
    }

    @FXML
    public void pinFriend() {
        if (pin.getGraphic() == notPinned) {
            pin.setGraphic(pinned);
            prefs.setPinned(user, true);
        } else {
            pin.setGraphic(notPinned);
            prefs.setPinned(user, false);
        }
        if (onPinChanged != null) {
            onPinChanged.accept(user);
        }
    }
}
