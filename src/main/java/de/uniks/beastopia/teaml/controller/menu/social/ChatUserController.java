package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.function.Consumer;

public class ChatUserController extends Controller {

    @FXML
    public ImageView chatAvatar;
    @FXML
    HBox _rootElement;
    @FXML
    Button pinGroupBtn;
    @FXML
    Button deleteGroupBtn;
    @FXML
    Text name;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;
    @Inject
    GroupListService groupListService;
    @Inject
    ImageService imageService;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    private Group group;
    private ImageView pinnedImg;
    private ImageView notPinnedImg;
    private Consumer<Group> onPinChanged = null;
    private Consumer<Group> onGroupClicked = null;

    @Inject
    public ChatUserController() {

    }

    public void setOnGroupClicked(Consumer<Group> onGroupClicked) {
        this.onGroupClicked = onGroupClicked;
    }

    public void setOnPinChanged(Consumer<Group> onPinChanged) {
        this.onPinChanged = onPinChanged;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        pinnedImg = imageService.getPinnedImage();
        notPinnedImg = imageService.getNotPinnedImage();

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

    public void mouseClicked() {
        onGroupClicked.accept(group);
    }

    public void deleteGroup() {
        if (group.members().size() < 2) {
            disposables.add(groupListService.deleteGroup(group).observeOn(FX_SCHEDULER).subscribe(
                    lr -> app.show(directMessageControllerProvider.get()),
                    error -> Dialog.error(error, resources.getString("deleteFailed")
                    )));
        } else {
            disposables.add(groupListService.removeMember(group, tokenStorage.getCurrentUser()._id()).observeOn(FX_SCHEDULER).subscribe(
                    lr -> app.show(directMessageControllerProvider.get()),
                    error -> Dialog.error(error, resources.getString("deleteFailed")
                    )));
        }
    }

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
