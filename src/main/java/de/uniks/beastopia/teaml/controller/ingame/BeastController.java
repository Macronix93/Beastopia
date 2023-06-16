package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.function.Consumer;

public class BeastController extends Controller {
    @Inject
    PresetsService presetsService;
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
        /*disposables.add(presetsService.getMonsterType(monster._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> {
                    name.setText(monsterType.name());

                }));*/

        hp.setText("HP: " + monster.currentAttributes().health() + " / " + monster.attributes().health());
        level.setText(String.valueOf(monster.level()));

        return parent;
    }

    @FXML
    public void toggleDetails() {
        onBeastClicked.accept(monster);
    }
}
