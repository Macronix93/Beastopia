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

        disposables.add(presetsService.getMonsterImage(winner1.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> winnerImg.setImage(monsterImage)));

        if (winner2 != null) {
            ImageView secondWinner = new ImageView();
            secondWinner.setX(125);
            secondWinner.setY(125);
            disposables.add(presetsService.getMonsterImage(winner2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(secondWinner::setImage));
            leftMonsterContainer.getChildren().add(secondWinner);
        }

        disposables.add(presetsService.getMonsterImage(loser1.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> loserImg.setImage(monsterImage)));

        if (loser2 != null) {
            ImageView secondLoser = new ImageView();
            secondLoser.setX(125);
            secondLoser.setY(125);
            disposables.add(presetsService.getMonsterImage(loser2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(secondLoser::setImage));
            rightMonsterContainer.getChildren().add(secondLoser);
        }

        if (isWinner) {
            resultLabel.setText(resources.getString("fightWon"));
        } else {
            resultLabel.setText(resources.getString("fightLost"));
        }

        return parent;
    }

    public void onLeaveButtonClicked() {
        app.show(ingameControllerProvider.get());
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

}
