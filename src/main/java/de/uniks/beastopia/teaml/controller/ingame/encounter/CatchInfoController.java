package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class CatchInfoController extends Controller {
    @FXML
    public Label catchInfoLabel;
    @FXML
    public ImageView catchImage;
    @FXML
    public Label catchToTeamLabel;
    private Runnable onCloseRequest;
    private String catchInfo;
    private String catchToTeam;
    private int beastType;

    @Inject
    public CatchInfoController() {
    }

    public void setCatchInfo(String catchInfo, String catchToTeam, int beastType) {
        this.catchInfo = catchInfo;
        this.catchToTeam = catchToTeam;
        this.beastType = beastType;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        catchInfoLabel.setText(catchInfo);
        catchToTeamLabel.setText(catchToTeam);
        //TODO image
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
