package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class ChatUserController extends Controller {

    @FXML
    HBox _rootElement;
    @FXML
    Button pinGroupBtn;
    @FXML
    Button deleteGroupBtn;
    @FXML
    Button editGroupBtn;
    @FXML
    Text name;

    private Group group;
    //private Boolean pinned;
    private ImageView pinnedImg;
    private ImageView notPinnedImg;
    //private ImageView abort;

    @Inject
    TokenStorage tokenStorage;
    @Inject
    FriendListService friendListService;

    @Inject
    Prefs prefs;

    private final Consumer<Group> onPinChanged = null;

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

    @SuppressWarnings("UnusedReturnValue")
    public ChatUserController setGroup(Group group, boolean pinned) {
        this.group = group;
        //this.pinned = pinned;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        String otherID = group.members().get(0).equals(tokenStorage.getCurrentUser()._id())
                ? group.members().get(1)
                : group.members().get(0);
        disposables.add(friendListService.getUser(otherID).subscribe(user -> name.setText(user.name())));

        if (prefs.isPinned(this.group)) {
            this.pinGroupBtn.setGraphic(pinnedImg);
            System.out.println("pinned");
        } else {
            this.pinGroupBtn.setGraphic(notPinnedImg);
            System.out.println("not pinned");
        }

        return parent;
    }

    public void mouseClicked() {
        onGroupClicked.accept(group);
    }

    private ImageView createImage(String imageUrl) throws URISyntaxException, FileNotFoundException {
        ImageView imageView = new ImageView(imageUrl);
        imageView.setFitHeight(25.0);
        imageView.setFitWidth(25.0);
        return imageView;
    }

    private static Image loadImage(URL imageUrl) throws FileNotFoundException, URISyntaxException {
        return new Image(new FileInputStream(new File(imageUrl.toURI())));
    }

    public void editGroup() {
        //TODO: show edit group dialog
    }

    public void deleteGroup() {
        //TODO: delete group
    }

    //TODO: implement pinning feature
    @FXML
    public void pinGroup() {
        if (!prefs.isPinned(this.group)) {
            pinGroupBtn.setGraphic(pinnedImg);
            prefs.setPinned(this.group, true);
            System.out.println(group.name() + " pinned");
        } else {
            pinGroupBtn.setGraphic(notPinnedImg);
            prefs.setPinned(this.group, false);
            System.out.println(group.name() + " unpinned");
        }


        //noinspection ConstantValue
        if (onPinChanged != null) {
            onPinChanged.accept(group);
        }
    }
}
