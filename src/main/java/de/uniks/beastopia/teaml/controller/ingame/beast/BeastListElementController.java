package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class BeastListElementController extends ListCell<Monster> {

    @FXML
    public ImageView beastImg;
    @FXML
    public Label beastLabel;
    @FXML
    public ProgressBar expProgress;
    @FXML
    public Button addBeast;
    @FXML
    public Button removeBeast;
    @FXML
    public GridPane gridPane;

    @Override
    protected void updateItem(Monster item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("views/ingame/beast/BeastListElement.fxml")));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            beastLabel.setText(item._id());
            expProgress.setProgress((double) 50 / 100);
            addBeast.setOnAction(event -> addBeastButtonClicked());
            removeBeast.setOnAction(event -> removeBeastButtonClicked());
        }
        setText(null);
        setGraphic(gridPane);
    }

    public void addBeastButtonClicked() {
        MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
        MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
        getListView().getItems().add(new Monster(null, null, "MONSTER_2", "TRAINER_ID", 0, 0, 0, currentAttributes, attributes));
        getListView().refresh();
        System.out.println("addBeastButtonClicked");
    }

    public void removeBeastButtonClicked() {
        getListView().getItems().remove(getItem());
        getListView().refresh();
        System.out.println("removeBeastButtonClicked");
    }
}
