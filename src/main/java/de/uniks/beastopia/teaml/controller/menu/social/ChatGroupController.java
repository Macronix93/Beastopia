package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
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

public class ChatGroupController extends Controller {

    @FXML
    HBox _rootElement;

    @FXML
    Button pinGroup;
    @FXML
    Button deleteGroup;
    @FXML
    Button editGroup;

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

    private Consumer<Group> onGroupClicked = null;

    @Inject
    public ChatGroupController() {

    }

    @Override
    public void init() {
        try {
            pinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/filled_pin.png")));
            notPinned = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/pin.png")));
            abort = createImage(Objects.requireNonNull(Main.class.getResource("assets/buttons/abort.png")));
        } catch (URISyntaxException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnGroupClicked(Consumer<Group> onGroupClicked) {
        this.onGroupClicked = onGroupClicked;
    }

    public ChatGroupController setGroup(Group group, boolean groupPin) {
        this.group = group;
        this.groupPin = groupPin;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        name.setText(group.name());
        return parent;
    }

    public void mouseClicked() {
        onGroupClicked.accept(group);
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


    public void editGroup() {
        //app.show(editGroupControllerProvider.get());
    }

    public void deleteGroup() {
        //todo: delete group
    }

    public void pinGroup() {
        if (pinGroup.getGraphic() == notPinned) {
            pinGroup.setGraphic(pinned);
            preferences.putBoolean(this.group._id() + "_pinned", true);
        } else {
            pinGroup.setGraphic(notPinned);
            preferences.putBoolean(this.group._id() + "_pinned", false);
        }
        if (onPinChanged != null) {
            onPinChanged.accept(group);
        }
    }
}
