package de.uniks.beastopia.teaml.controller.menu.social;


import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.ImageService;
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
import java.util.Objects;
import java.util.function.Consumer;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;

public class FriendController extends Controller {
    @FXML
    public ImageView chatImage;
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
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    FriendListService friendListService;
    @Inject
    Prefs prefs;
    @Inject
    EventListener eventListener;
    @Inject
    ImageService imageService;
    @Inject
    DataCache cache;
    private User user;
    private Boolean friendPin;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView addImage;
    private ImageView removeImage;
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

        addImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/plus.png")).toString());
        removeImage = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/minus.png")).toString());
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
        chatImage.setImage(imageService.getThemeImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/buttons/chat.png")))));
        pinned = imageService.getPinnedImage();
        notPinned = imageService.getNotPinnedImage();
        friendAvatar.setImage(cache.getImageAvatar(user));
        name.setText(user.name());
        updateOnlineStatus(user);

        if (friendListService.isFriend(user) || friend) {
            if (this.friendPin) {
                this.pin.setGraphic(pinned);
            } else {
                this.pin.setGraphic(notPinned);
            }
        } else {
            this.pin.setGraphic(notPinned);
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

    private ImageView createImage(String imageUrl) {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setCache(false);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
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

    @Override
    public void destroy() {
        pinned = null;
        notPinned = null;
        addImage = null;
        removeImage = null;
        onPinChanged = null;
        onFriendChanged = null;
        friendAvatar = null;

        super.destroy();
    }
}
