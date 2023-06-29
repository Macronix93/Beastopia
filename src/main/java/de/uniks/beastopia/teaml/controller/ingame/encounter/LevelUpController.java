package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class LevelUpController extends Controller {
    @FXML
    public Label headline;
    @FXML
    public ImageView image;
    @FXML
    public Label up_text;
    @FXML
    public Label attack;
    @FXML
    public Label type;
    @FXML
    public Label accuracy;
    @FXML
    public Label power;
    @FXML
    public Label up_text_bottom;
    @FXML
    public Label lifeValueLabel;
    @FXML
    public Label maxLifeLabel;
    @FXML
    public Label plusHP;
    @FXML
    public Label xpValueLabel;
    @FXML
    public Label maxXpLabel;
    @FXML
    public Button continueBtn;
    @Inject
    TrainerService trainerService;

    @Inject
    PresetsService presetsService;

    @Inject
    Prefs prefs;

    private Monster beast;

    private boolean newAbility;
    private boolean dev;

    @Inject
    public LevelUpController() {

    }

    public void setBeast(Monster beast, boolean newAbility, boolean dev) {
        //TODO evtl. alten Type für Name und Bild übergeben und um diff. maxhp. zu haben
        this.newAbility = newAbility;
        this.beast = beast;
        this.dev = dev;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        headline.setText("Level up!");

        disposables.add(presetsService.getMonsterType(beast.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    up_text_bottom.setText(type.name() + " (" + type.type() + ") Lvl. " + beast.level());
                    if (prefs.getLocale().contains("de")) {
                        if (this.newAbility) {
                            up_text.setText(type.name() + " ist ein Level aufgestiegen und erlernt die Fähigkeit " + "FÄHIGKEIT");
                        } else {
                            up_text.setText(type.name() + " ist ein Level aufgestiegen!");
                        }
                    } else {
                        if (this.newAbility) {
                            up_text.setText(type.name() + " levelled up and unlocked the new ability " + "FÄHIGKEIT");
                        } else {
                            up_text.setText(type.name() + " levelled up!");
                        }
                    }
                }));

        lifeValueLabel.setText(beast.currentAttributes().health() + " ");
        maxLifeLabel.setText(" " + beast.currentAttributes().health());
        //TODO plusHP.setText();
        xpValueLabel.setText(beast.experience() + " ");
        maxXpLabel.setText((int) Math.pow(beast.level(), 3) - (int) Math.pow(beast.level() - 1, 3) + " ");

        if (dev) { //Fade old Image -> New one
            //Fade out oldImage //vllt id - 1
            disposables.add(presetsService.getMonsterImage(beast.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage ->
                            //TODO Fade in new
                            image.setImage(monsterImage)));
        } else {
            disposables.add(presetsService.getMonsterImage(beast.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> image.setImage(monsterImage))); //TODO fade
        }

        if (this.newAbility) {
            //TODO wenn neue nicht hinten ist
            disposables.add(presetsService.getAbility(beast.abilities().get(beast.abilities().size()-1))
                    .observeOn(FX_SCHEDULER)
                    .subscribe(abilityDto -> {
                        attack.setText(abilityDto.name());
                        accuracy.setText("Accuracy: " + abilityDto.accuracy());
                        type.setText("Type: " + abilityDto.type());
                        power.setText("Power: " + abilityDto.power());
                    }));
        }

        return parent;
    }


    @FXML
    public void continuePressed() {
    }
}
