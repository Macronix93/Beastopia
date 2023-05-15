package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class ChatGroupController extends Controller {

    @FXML
    HBox _rootElement;

    @FXML
    Button pin;

    @FXML
    Text name;

    private User user;
    //private Group group;

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
    public ChatGroupController() {

    }

    @FXML
    public void pinGroup() {
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
