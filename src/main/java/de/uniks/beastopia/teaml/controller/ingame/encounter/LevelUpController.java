package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

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
    public Label plusHPLabel;
    @FXML
    public Label xpValueLabel;
    @FXML
    public Label maxXpLabel;
    @FXML
    public Button continueBtn;
    @FXML
    public Label abilityLabel;
    @FXML
    public ImageView heart;
    @FXML
    public ImageView star;
    @FXML
    public HBox hpBg;
    @FXML
    public HBox starBg;
    @FXML
    public HBox borderBg;

    @Inject
    PresetsService presetsService;

    @Inject
    Prefs prefs;

    private Monster beast;

    private boolean newAbility;
    private boolean dev;
    private int plusHP;

    @Inject
    public LevelUpController() {

    }

    public void setBeast(Monster beast, boolean newAbility, boolean dev, int hp) {
        this.newAbility = newAbility;
        this.beast = beast;
        this.dev = dev;
        this.plusHP = hp;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        headline.setText("Level up!");
        heart.setImage(new Image("de/uniks/beastopia/teaml/assets/herz.png"));
        heart.setFitWidth(25);
        heart.setFitHeight(25);

        star.setImage(new Image("de/uniks/beastopia/teaml/assets/star.png"));
        star.setFitWidth(25);
        star.setFitHeight(25);

        disposables.add(presetsService.getMonsterType(beast.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    up_text_bottom.setText(type.name() + " " + type.type() + " Lvl. " + beast.level());
                    if (prefs.getLocale().contains("de")) {
                        if (this.newAbility) {
                            up_text.setText(type.name() + " ist ein Level aufgestiegen und erlernt die Fähigkeit ");
                        } else {
                            up_text.setText(type.name() + " ist ein Level aufgestiegen!");
                        }
                    } else {
                        if (this.newAbility) {
                            up_text.setText(type.name() + " levelled up and unlocked the new ability ");
                        } else {
                            up_text.setText(type.name() + " levelled up!");
                        }
                    }
                }));

        lifeValueLabel.setText(beast.currentAttributes().health() + " ");
        maxLifeLabel.setText(" " + beast.attributes().health());

        double healthWidth = (double) beast.currentAttributes().health() / beast.attributes().health();
        hpBg.setPrefWidth(healthWidth * borderBg.getWidth());

        plusHPLabel.setText(" (+" + plusHP + " Max HP)");
        xpValueLabel.setText(beast.experience() + " ");
        int maxExp = (int) Math.pow(beast.level(), 3) - (int) Math.pow(beast.level() - 1, 3);
        maxXpLabel.setText(maxExp + " ");
        double expWidth = (double) beast.experience() / maxExp;
        starBg.setPrefWidth(expWidth * borderBg.getWidth());

        if (dev) { //Fade old Image -> New one
            disposables.add(presetsService.getMonsterImage(beast.type() - 1)
                    .observeOn(FX_SCHEDULER)
                    .subscribe(beforeImg -> {
                        image.setImage(beforeImg);

                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), image);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);

                        disposables.add(presetsService.getMonsterImage(beast.type())
                                .observeOn(FX_SCHEDULER)
                                .subscribe(afterImage -> {
                                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), image);
                                    fadeIn.setFromValue(0.0);
                                    fadeIn.setToValue(1.0);

                                    fadeOut.setOnFinished(event -> { // set new img and start fade
                                        image.setImage(afterImage);
                                        fadeIn.play();
                                    });
                                    fadeOut.play();
                                }));
                    }));
        } else {
            disposables.add(presetsService.getMonsterImage(beast.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> image.setImage(monsterImage)));
        }

        if (this.newAbility) {
            disposables.add(presetsService.getAbility(beast.abilities().get(beast.abilities().size() - 1))
                    .observeOn(FX_SCHEDULER)
                    .subscribe(abilityDto -> {
                        attack.setText(abilityDto.name());
                        abilityLabel.setText(abilityDto.name());
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
