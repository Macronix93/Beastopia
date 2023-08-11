package de.uniks.beastopia.teaml.controller.ingame.beastlist;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.AssetProvider;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import java.util.function.Consumer;

public class BeastController extends Controller {
    @FXML
    public HBox statusPocket;
    @FXML
    public GridPane beast;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;
    @Inject
    AssetProvider assets;
    @FXML
    public ImageView avatar;
    @FXML
    public Label name;
    @FXML
    public Label hp;
    @FXML
    public Label level;
    private Monster monster;
    private Consumer<Monster> onBeastClicked;

    @Inject
    public BeastController() {
    }

    public void setOnBeastClicked(Consumer<Monster> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
    }

    public BeastController setBeast(Monster monster) {
        this.monster = monster;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        name.setText(cache.getBeastDto(monster.type()).name());
        if (!cache.imageIsDownloaded(monster.type())) {
            Image monsterImage = presetsService.getMonsterImage(monster.type()).blockingFirst();
            cache.addMonsterImages(monster.type(), monsterImage);
            avatar.setImage(monsterImage);
        } else {
            avatar.setImage(cache.getMonsterImage(monster.type()));
        }

        hp.setText("HP: " + (int) monster.currentAttributes().health() + " / " + (int) monster.attributes().health());
        level.setText(String.valueOf(monster.level()));
        statusPocket.getChildren().clear();
        monster.status().forEach(status -> statusPocket.getChildren().add(assets.getIcon("status", status, 20, 20)));

        return parent;
    }

    @FXML
    public void toggleDetails() {
        onBeastClicked.accept(monster);
    }
}
