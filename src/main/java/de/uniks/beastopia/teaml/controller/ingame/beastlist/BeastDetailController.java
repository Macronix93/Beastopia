package de.uniks.beastopia.teaml.controller.ingame.beastlist;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Map;

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
    @FXML
    public VBox VBoxBeastDetail;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;
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
        level.setText("Level: " + (monster.level()));
        int maxExp = (int) Math.round(Math.pow(monster.level(), 3) - Math.pow((monster.level() - 1), 3));
        experience.setText("EXP: " + monster.experience() + " / " + maxExp);
        speed.setText("Speed: " + monster.currentAttributes().speed());
        attack.setText("Attack: " + monster.currentAttributes().attack());
        defense.setText("Defense: " + monster.currentAttributes().defense());
        hp.setText("HP: " + (int) monster.currentAttributes().health() + " / " + (int) monster.attributes().health());

        description.setEditable(false);
        description.setFocusTraversable(false);

        disposables.add(presetsService.getMonsterType(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterType -> {
                    name.setText(monsterType.name());
                    type.setText("Type: " + monsterType.type().get(0));
                    description.setText(monsterType.description() + "\n\n");

                    for (Map.Entry<String, Integer> entry : monster.abilities().entrySet()) {
                        int ability = Integer.parseInt(entry.getKey());

                        if (cache.getAbilities().containsKey(ability)) {
                            for (Map.Entry<Integer, AbilityDto> cacheEntry : cache.getAbilities().entrySet()) {
                                if (cacheEntry.getKey() == ability) {
                                    description.setText(description.getText() + cacheEntry.getValue().name() + " [" + cacheEntry.getValue().type() +
                                            "] ACC: " + cacheEntry.getValue().accuracy() + " POW: " + cacheEntry.getValue().power() + "\n" + cacheEntry.getValue().description() + "\n\n");
                                    break;
                                }
                            }
                        } else {
                            disposables.add(presetsService.getAbility(ability)
                                    .observeOn(FX_SCHEDULER)
                                    .subscribe(ad -> {
                                        cache.getAbilities().put(ability, ad);
                                        description.setText(description.getText() + ad.name() + " [" + ad.type() +
                                                "] ACC: " + ad.accuracy() + " POW: " + ad.power() + "\n" + ad.description() + "\n\n");
                                    }));
                        }
                    }
                }));

        disposables.add(presetsService.getMonsterImage(monster.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> ImageViewAvatar.setImage(monsterImage)));

        return parent;
    }
}
