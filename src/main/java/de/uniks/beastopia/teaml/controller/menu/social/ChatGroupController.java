package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import javafx.fxml.FXML;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class ChatGroupController extends Controller {

    @FXML
    HBox _rootElement;

    @FXML
    Button pin;

    @FXML
    Text name;

    private Group group;

    private Boolean groupPin;
    private ImageView pinned;
    private ImageView notPinned;
    private ImageView abort;

    @Inject
    Preferences preferences;

    private Consumer<Group> onPinChanged = null;

    @Inject
    public ChatGroupController() {

    }

    @Override
    public void init() {
        ArrayList<String> groupList = new ArrayList<>();
        groupList.add("user1");
        groupList.add("user2");
        Group group = new Group(null, null, "null", null, groupList);
        this.group = group;
        this.groupPin = true;

        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")));
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")));
            abort = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/abort.png")));
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatGroupController setGroup(Group group, boolean groupPin) {
        this.group = group;
        this.groupPin = groupPin;
        return this;
    }

    @FXML
    public void showEditGroup() {
        //app.show(editGroupControllerProvider.get());
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
    public void pinGroup() {
        if (pin.getGraphic() == notPinned) {
            pin.setGraphic(pinned);
            preferences.putBoolean(this.group._id() + "_pinned", true);
        } else {
            pin.setGraphic(notPinned);
            preferences.putBoolean(this.group._id() + "_pinned", false);
        }
        if (onPinChanged != null) {
            onPinChanged.accept(group);
        }
    }
}
