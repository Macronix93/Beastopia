package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;

public class JoinFightInfoController extends Controller {
    @FXML
    VBox infoSign;
    @FXML
    VBox infoTextBox;
    @FXML
    Text helpCaption;
    @FXML
    Text helpText;
    @FXML
    ImageView pointingArrow;

    private double posX;
    private double posY;
    private Pane parent;

    @Inject
    public JoinFightInfoController() {
    }

    @Override
    public void init() {
        super.init();

        startTimer(20);
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        infoSign.setTranslateX(posX - 110);
        infoSign.setTranslateY(posY + 32);

        infoTextBox.getChildren().clear();
        infoTextBox.getChildren().addAll(helpCaption, helpText);

        pointingArrow.setRotate(30);

        helpCaption.setText(resources.getString("helpTrainerCaption"));
        helpCaption.setStyle("-fx-font-size: 16px;");
        helpText.setText(resources.getString("helpTrainerText"));
        helpText.setStyle("-fx-font-size: 12px;");

        return parent;
    }

    public void setX(double x) {
        this.posX = x;
    }

    public void setY(double y) {
        this.posY = y;
    }

    public void setParent(Pane parent) {
        this.parent = parent;
    }

    public void startTimer(int seconds) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(seconds), event -> onTimerFinished()));
        timeline.play();
    }

    private void onTimerFinished() {
        parent.getChildren().remove(infoSign);
        destroy();
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
