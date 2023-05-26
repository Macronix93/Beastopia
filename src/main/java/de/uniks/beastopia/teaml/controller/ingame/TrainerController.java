package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class TrainerController extends Controller {
    @FXML
    private VBox trainerContainer;
    @FXML
    private TextField trainerNameInput;
    @FXML
    private Text regionNameDisplay;
    @FXML
    private ImageView trainerSprite;
    @FXML
    private Button backButton;
    @FXML
    private Button chooseLeftButton;
    @FXML
    private Button chooseRightButton;
    @FXML
    private Button deleteTrainerButton;
    @FXML
    private Button saveTrainerButton;

    @Inject
    TokenStorage tokenStorage;

    private final SimpleStringProperty trainerName = new SimpleStringProperty();

    @Inject
    public TrainerController() {
    }

    public void saveTrainer() {
        //TODO: Save trainer for current region
    }

    public void deleteTrainer() {
        //TODO: Delete trainer for current region
    }

    public void back() {
        //TODO: Implement back functionality
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        trainerNameInput.textProperty().bindBidirectional(trainerName);
        regionNameDisplay.setText("RegionName");
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleTrainer");
    }
}
