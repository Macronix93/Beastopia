package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
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

    private Monster winner1;
    private Monster winner2;
    private Monster loser1;
    private Monster loser2;
    private boolean isWinner = false;

    @Inject
    public EndScreenController() {
    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

/*
        disposables.add(presetsService.getMonsterImage(winner1.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> winnerImg.setImage(monsterImage)));

        if (winner2 != null) {
            ImageView secondWinner = new ImageView();
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
            disposables.add(presetsService.getMonsterImage(loser2.type())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(secondLoser::setImage));
            leftMonsterContainer.getChildren().add(secondLoser);
        }
*/
        //resultLabel.setTextOverrun(OverrunStyle.);

        if (isWinner) {
            resultLabel.setText("You won the fight!");
        } else {
            resultLabel.setText("You lost the fight!");
        }

        return parent;
    }

    public void onLeaveButtonClicked() {
        app.show(ingameControllerProvider.get());
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void setWinnerMonster1(Monster winner1) {
        this.winner1 = winner1;
    }

    public void setWinnerMonster2(Monster winner2) {
        this.winner2 = winner2;
    }

    public void setLoserMonster1(Monster loser1) {
        this.loser1 = loser1;
    }

    public void setLoserMonster2(Monster loser2) {
        this.loser2 = loser2;
    }

}
