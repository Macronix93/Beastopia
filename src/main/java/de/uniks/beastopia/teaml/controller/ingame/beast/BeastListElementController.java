package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
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
    public GridPane gridPane;
    @FXML
    public Button upButton;
    @FXML
    public Button downButton;
    @Inject
    PresetsService presetsService;

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BeastListElementController() {
        super();
        /*disposables.add(presetsService.getMonsterImage(0)
                .subscribe(image -> Platform
                        .runLater(() -> {
                            beastImg.setImage(image);
                            disposables.dispose();
                        })));*/
    }


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
            beastLabel.setText("Name (" + "type" + ") Lv. " + item.level());
            int maxExp = (int) Math.round(Math.pow(item.level(), 3) - Math.pow((item.level() - 1), 3));
            expProgress.setProgress((double) item.experience() / maxExp);
            setText(null);
            setGraphic(gridPane);
            upButton.setOnAction(event -> {
                int index = getListView().getItems().indexOf(item);
                swap(index, index - 1);
            });
            downButton.setOnAction(event -> {
                int index = getListView().getItems().indexOf(item);
                swap(index, index + 1);
            });
        }
    }

    private void swap(int index1, int index2) {
        if (index1 < 0
                || index2 < 0
                || index1 >= getListView().getItems().size()
                || index2 >= getListView().getItems().size()) {
            return;
        }
        Monster monster1 = getListView().getItems().get(index1);
        Monster monster2 = getListView().getItems().get(index2);
        getListView().getItems().set(index1, monster2);
        getListView().getItems().set(index2, monster1);
        getListView().refresh();
    }
}
