package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

// suppressed due to wrongly marked warning
@SuppressWarnings("unused")
public class BeastListElementController extends ListCell<Monster> {

    protected static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);

    @FXML
    public ImageView beastImg;
    @FXML
    public Label beastLabel;
    @FXML
    public ProgressBar hpBar;
    @FXML
    public ProgressBar expProgress;
    @FXML
    public GridPane gridPane;
    @FXML
    public Button upButton;
    @FXML
    public Button downButton;
    @FXML
    public HBox buttonBox;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BeastListElementController() {
    }


    @Override
    protected void updateItem(Monster item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("views/ingame/beast/BeastListElement.fxml")));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (cache.imageIsDownloaded(item.type())) {
                beastImg.setImage(cache.getMonsterImage(item.type()));
            } else {
                disposables.add(presetsService.getMonsterImage(item.type())
                        .observeOn(FX_SCHEDULER)
                        .subscribe(image -> {
                                    cache.addMonsterImages(item.type(), image);
                                    beastImg.setImage(image);
                                },
                                Throwable::printStackTrace));
            }

            MonsterTypeDto type = cache.getBeastDto(item.type());
            beastLabel.setText(type.name() + " (" + type.type().get(0) + ") Lv. " + item.level());
            int maxExp = (int) Math.round(Math.pow(item.level(), 3) - Math.pow((item.level() - 1), 3));
            expProgress.setProgress((double) item.experience() / maxExp);
            hpBar.setProgress(item.currentAttributes().health() / item.attributes().health());
            upButton.setOnAction(event -> {
                int index = getListView().getItems().indexOf(item);
                swap(index, index - 1);
            });
            downButton.setOnAction(event -> {
                int index = getListView().getItems().indexOf(item);
                swap(index, index + 1);
            });
            if (getListView().getId().equals("beastListView")) {
                buttonBox.getChildren().removeAll(upButton, downButton);
                expProgress.setPrefWidth(150);
            }
            setGraphic(gridPane);
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
