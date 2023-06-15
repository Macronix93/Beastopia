package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.PresetsApiService;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.inject.Inject;

public class BeastDetailController extends Controller {

    @FXML
    public Label name;
    @FXML
    public Label hp;
    @FXML
    public Label speed;
    @FXML
    public Label level;
    @FXML
    public Label type;
    @FXML
    public Label attack;
    @FXML
    public Label experience;
    @FXML
    public Label defense;
    @FXML
    public TextArea description;
    @Inject
    PresetsService presetsService;
    private Monster monster;

    @Inject
    public BeastDetailController() {

    }

    public void setBeast(Monster monster) {
        this.monster = monster;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        level.setText(String.valueOf(monster.level()));

        disposables.add(presetsService.getMonsterType(monster._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> {
                    name.setText(monsterType.name());
                    type.setText(monsterType.type().get(0));
                    description.setText(monsterType.description());
                }));

        return parent;
    }
}
