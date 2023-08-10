package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class EndScreenController extends Controller {

    @FXML
    Button leaveButton;
    @FXML
    Label resultLabel;
    @FXML
    ImageView winnerImg;
    @FXML
    ImageView loserImg;
    @FXML
    VBox leftMonsterContainer;
    @FXML
    VBox rightMonsterContainer;
    @FXML
    Label coinInfo;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    @Inject
    PresetsService presetsService;
    @SuppressWarnings("unused")
    @Inject
    DataCache dataCache;
    @SuppressWarnings("unused")
    @Inject
    Prefs prefs;

    private Monster winner1;
    private Monster winner2;
    private Monster loser1;
    private Monster loser2;
    private boolean isWinner = false;
    private String gainedCoins = "";

    @Inject
    public EndScreenController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEncounter");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        getEndScreenImages(winner1, winnerImg, winner2, leftMonsterContainer);
        getEndScreenImages(loser1, loserImg, loser2, rightMonsterContainer);

        if (isWinner) {
            resultLabel.setText(resources.getString("fightWon"));
        } else {
            resultLabel.setText(resources.getString("fightLost"));
        }

        coinInfo.setText(gainedCoins);
        dataCache.setCurrentEncounter(null);

        return parent;
    }

    private void getEndScreenImages(Monster monster1, ImageView winnerImg, Monster monster2, VBox leftMonsterContainer) {
        if (!dataCache.imageIsDownloaded(monster1.type())) {
            Image monsterImage = presetsService.getMonsterImage(monster1.type()).blockingFirst();
            dataCache.addMonsterImages(monster1.type(), monsterImage);
            winnerImg.setImage(monsterImage);
        } else {
            winnerImg.setImage(dataCache.getMonsterImage(monster1.type()));
        }

        if (monster2 != null) {
            ImageView secondWinner = new ImageView();
            secondWinner.setX(125);
            secondWinner.setY(125);
            if (!dataCache.imageIsDownloaded(monster2.type())) {
                Image monsterImage = presetsService.getMonsterImage(monster2.type()).blockingFirst();
                dataCache.addMonsterImages(monster2.type(), monsterImage);
                secondWinner.setImage(monsterImage);
            } else {
                secondWinner.setImage(dataCache.getMonsterImage(monster2.type()));
            }
            leftMonsterContainer.getChildren().add(secondWinner);
        }
    }

    public void onLeaveButtonClicked() {
        IngameController controller = ingameControllerProvider.get();
        controller.setRegion(dataCache.getJoinedRegion());
        app.show(controller);
    }

    @SuppressWarnings("unused")
    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    @SuppressWarnings("unused")
    public void setWinnerMonster1(Monster winner1) {
        this.winner1 = winner1;
    }

    @SuppressWarnings("unused")
    public void setWinnerMonster2(Monster winner2) {
        this.winner2 = winner2;
    }

    @SuppressWarnings("unused")
    public void setLoserMonster1(Monster loser1) {
        this.loser1 = loser1;
    }

    @SuppressWarnings("unused")
    public void setLoserMonster2(Monster loser2) {
        this.loser2 = loser2;
    }

    public void setGainedCoins(String gainedCoins) {
        this.gainedCoins = gainedCoins;
    }

}
