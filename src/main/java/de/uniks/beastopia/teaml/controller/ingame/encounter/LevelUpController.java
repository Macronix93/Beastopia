package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.AssetProvider;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

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
    @FXML
    public VBox beastInfo;
    @FXML
    public VBox abilityInfo;
    @FXML
    public ImageView status;

    @Inject
    PresetsService presetsService;
    AssetProvider assets;
    private Monster beast;
    private boolean newAbility;
    private boolean dev;
    private int plusHP;
    private double healthWidth;
    private double expWidth;

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
    public void onResize(int width, int height) {
        hpBg.setMinWidth(healthWidth * borderBg.getWidth());
        hpBg.setMaxWidth(healthWidth * borderBg.getWidth());
        starBg.setMinWidth(expWidth * borderBg.getWidth());
        starBg.setMaxWidth(expWidth * borderBg.getWidth());
    }
    @Override
    public Parent render() {
        Parent parent = super.render();
        headline.setText("Level up!");
        heart.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/herz.png"))));
        heart.setFitWidth(25);
        heart.setFitHeight(25);

        star.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/star.png"))));
        star.setFitWidth(25);
        star.setFitHeight(25);

        if (!beast.status().isEmpty()) {
            status.setImage(assets.getStatusIcon(beast.status().get(0)));
        }

        disposables.add(presetsService.getMonsterType(beast.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(type -> {
                    up_text_bottom.setText(type.name() + " " + type.type() + " Lvl. " + beast.level());
                    if (this.newAbility) {
                        up_text.setText(type.name() + " " + resources.getString("lvl+A"));
                    } else {
                        up_text.setText(type.name() + " " + resources.getString("lvl+"));
                    }
                }));

        lifeValueLabel.setText(beast.currentAttributes().health() + " ");
        maxLifeLabel.setText(" " + beast.attributes().health());
        plusHPLabel.setText(" (+" + plusHP + " Max HP)");
        xpValueLabel.setText(beast.experience() + " ");
        int maxExp = (int) Math.pow(beast.level(), 3) - (int) Math.pow(beast.level() - 1, 3);
        maxXpLabel.setText(maxExp + " ");

        healthWidth = (double) beast.currentAttributes().health() / beast.attributes().health();
        expWidth = (double) beast.experience() / maxExp;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    hpBg.setMinWidth(healthWidth * borderBg.getWidth());
                    hpBg.setMaxWidth(healthWidth * borderBg.getWidth());
                    starBg.setMinWidth(expWidth * borderBg.getWidth());
                    starBg.setMaxWidth(expWidth * borderBg.getWidth());
                });
            }
        }, 100);

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
            String lastKey = (String) beast.abilities().keySet().toArray()[beast.abilities().size() - 1];
            disposables.add(presetsService.getAbility(beast.abilities().get(lastKey))
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
