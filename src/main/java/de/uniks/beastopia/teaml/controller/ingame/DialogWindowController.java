package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.ResourceBundle;

public class DialogWindowController extends Controller {

    @FXML
    Text dialogText;
    @FXML
    VBox imageBox;
    @FXML
    HBox choiceBox;
    @FXML
    ImageView imageView;
    @Inject
    Provider<ResourceBundle> resourcesProvider;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    @Override
    public String getTitle() {
        return "titleDialogWindow";
    }

    public void start() {
        Image image = new Image("");
        imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(imageBox.widthProperty());
        imageView.fitHeightProperty().bind(imageBox.heightProperty());
    }

    @Override
    public Parent render() {
        Parent parent = super.render();


        return parent;
    }

    public void setChoices(List<String> choices) {
        for (String choice : choices) {
            Button button = new Button(choice);
            choiceBox.getChildren().add(button);
        }
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    @FXML
    public void back() {
        app.show(ingameControllerProvider.get());
    }
}
