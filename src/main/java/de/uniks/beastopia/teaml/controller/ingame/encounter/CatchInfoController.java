package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

@SuppressWarnings("unused")
public class CatchInfoController extends Controller {
    @FXML
    public ImageView catchImage;
    @FXML
    public Label catchToTeamLabel;
    @FXML
    public VBox catchInfoBg;
    @FXML
    public Label catchInfo;
    private Runnable onCloseRequest;
    private String catchInfoText;
    private String catchToTeam;
    private int beastType;
    @Inject
    PresetsService presetsService;

    @Inject
    public CatchInfoController() {
    }

    public CatchInfoController setCatchInfo(String catchInfoText, String catchToTeam, int beastType) {
        this.catchInfoText = catchInfoText;
        this.catchToTeam = catchToTeam;
        this.beastType = beastType;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(presetsService.getMonsterType(beastType).observeOn(FX_SCHEDULER).subscribe(
                t -> {
                    catchInfo.setText(catchInfoText + " " + t.name() + "!");
                    if (!catchToTeam.isEmpty()) {
                        catchToTeamLabel.setText(t.name() + " " + catchToTeam);
                    }
                    disposables.add(presetsService.getMonsterImage(t.id()).subscribe(
                            i -> catchImage.setImage(i)));
                }
        ));
        return parent;
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            onCloseRequest.run();
        }
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }
}
