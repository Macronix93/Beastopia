package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.function.Consumer;

public class ChatUserController extends Controller {

    @FXML
    public ImageView chatAvatar;
    @FXML
    HBox _rootElement;
    @FXML
    Button pinGroupBtn;
    @SuppressWarnings("unused")
    @FXML
    Button deleteGroupBtn;
    @SuppressWarnings("unused")
    @FXML
    Button editGroupBtn;
    @FXML
    Text name;

    @Inject
    TokenStorage tokenStorage;
    @Inject
    FriendListService friendListService;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;

    private Group group;
    private ImageView pinnedImg;
    private ImageView notPinnedImg;
    private Consumer<Group> onPinChanged = null;
    private Consumer<Group> onGroupClicked = null;

    @Inject
    public ChatUserController() {

    }

    @Override
    public void init() {
        try {
            pinnedImg = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")).toString());
            notPinnedImg = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")).toString());
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnGroupClicked(Consumer<Group> onGroupClicked) {
        this.onGroupClicked = onGroupClicked;
    }


    public void setOnPinChanged(Consumer<Group> onPinChanged) {
        this.onPinChanged = onPinChanged;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ChatUserController setGroup(Group group) {
        this.group = group;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        String otherID = group.members().get(0).equals(tokenStorage.getCurrentUser()._id())
                ? group.members().get(1)
                : group.members().get(0);

        cache.getAllUsers().stream()
                .filter(user -> user._id().equals(otherID))
                .findFirst()
                .ifPresent(user -> {
                    name.setText(user.name());
                    chatAvatar.setImage(cache.getImageAvatar(user));
                });

        if (prefs.isPinned(this.group)) {
            this.pinGroupBtn.setGraphic(pinnedImg);
        } else {
            this.pinGroupBtn.setGraphic(notPinnedImg);
        }

        return parent;
    }

    @SuppressWarnings("unused")
    public void mouseClicked() {
        onGroupClicked.accept(group);
    }

    private ImageView createImage(String imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }

    @SuppressWarnings("unused")
    public void editGroup() {
        //TODO: show edit group dialog
    }

    @SuppressWarnings("unused")
    public void deleteGroup() {
        //TODO: delete group
    }

    @SuppressWarnings("unused")
    @FXML
    public void pinGroup() {
        if (!prefs.isPinned(this.group)) {
            pinGroupBtn.setGraphic(pinnedImg);
            prefs.setPinned(this.group, true);
        } else {
            pinGroupBtn.setGraphic(notPinnedImg);
            prefs.setPinned(this.group, false);
        }
        if (onPinChanged != null) {
            onPinChanged.accept(group);
        }
    }

    @Override
    public void destroy() {
        chatAvatar = null;
        super.destroy();
    }
}
