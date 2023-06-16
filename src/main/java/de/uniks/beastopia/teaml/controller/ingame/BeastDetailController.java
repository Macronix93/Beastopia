package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

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
    @FXML
    public ImageView ImageViewAvatar;
    @Inject
    PresetsService presetsService;
    private Monster monster;
    private String image;
    @Inject
    public BeastDetailController() {

    }

    public void setBeast(Monster monster) {
        this.monster = monster;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        level.setText("Level: " + (monster.level()));
        //ToDo Rechnung anpassen
        int maxExp = monster.level() ^ 3 - ((monster.level() - 1) ^ 3);
        experience.setText("EXP: " + monster.experience() + " / " + maxExp);
        speed.setText("Speed: " + monster.currentAttributes().speed());
        attack.setText("Attack: " + monster.currentAttributes().attack());
        defense.setText("Defense: " + monster.currentAttributes().defense());
        hp.setText("HP: " + monster.currentAttributes().health() + " / " + monster.attributes().health());

        disposables.add(presetsService.getMonsterType(monster._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> {
                    name.setText(monsterType.name());
                    type.setText("Type: " + monsterType.type().get(0));
                    description.setText(monsterType.description());
                }));

        disposables.add(presetsService.getMonsterImage(monster._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> {
                    ImageViewAvatar.setImage(monsterImage);
                }));

        return parent;
    }
}
