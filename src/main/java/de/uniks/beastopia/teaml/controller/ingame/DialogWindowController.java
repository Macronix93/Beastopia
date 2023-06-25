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

    private Consumer<Integer> onButtonClicked;
    private Runnable onCloseRequested;
    private List<String> choices = new ArrayList<>();
    private List<Image> buttonImages = new ArrayList<>();
    private Image trainerImage;
    private String text;

    @Inject
    public DialogWindowController() {
    }

    public void setOnCloseRequested(Runnable onCloseRequested) {
        this.onCloseRequested = onCloseRequested;
    }

    public DialogWindowController setOnButtonClicked(Consumer<Integer> onButtonClicked) {
        this.onButtonClicked = onButtonClicked;
        return this;
    }

    public DialogWindowController setChoices(List<String> choices) {
        this.choices = choices;
        return this;
    }

    public DialogWindowController setTrainerImage(Image trainerImage) {
        this.trainerImage = trainerImage;
        return this;
    }

    public DialogWindowController setButtonImages(List<Image> images) {
        this.buttonImages = images;
        return this;
    }

    public DialogWindowController setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String getTitle() {
        return "titleDialogWindow";
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        for (int i = 0; i < choices.size(); i++) {
            AtomicInteger index = new AtomicInteger(i);
            if (buttonImages.isEmpty()) {
                Button choiceButton = new Button(choices.get(i));
                choiceButton.setOnAction(ev -> {
                    if (onButtonClicked != null) {
                        onButtonClicked.accept(index.get());
                    }
                });
                choiceBox.getChildren().add(choiceButton);
            } else {
                ImageView buttonImageView = new ImageView(buttonImages.get(i));
                buttonImageView.setFitWidth(64);
                buttonImageView.setFitHeight(64);
                Text choiceButton = new Text(choices.get(i));
                choiceButton.setTextAlignment(TextAlignment.CENTER);
                VBox buttonBox = new VBox();
                buttonBox.setOnMouseClicked(ev -> {
                    if (onButtonClicked != null) {
                        onButtonClicked.accept(index.get());
                    }
                });
                buttonBox.getChildren().add(choiceButton);
                buttonBox.getChildren().add(buttonImageView);
                choiceBox.getChildren().add(buttonBox);
            }
        }

        trainerImageView.setImage(trainerImage);
        dialogText.setText(text);

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
    public void close() {
        if (onCloseRequested != null) {
            onCloseRequested.run();
        }
    }
}
