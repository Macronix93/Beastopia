package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class EditBeastTeamController extends Controller {

    @FXML
    public ListView beastList;
    @FXML
    public TextField filterBar;
    @FXML
    public ListView beastTeam;
    @FXML
    public Button beastTeamBack;
    @FXML
    public Button editBeastTeam;
    @Inject
    App app;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;

    List<Monster> allMonster = new ArrayList<>();

    @Inject
    public EditBeastTeamController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    this.allMonster.addAll(monsters);
                    beastList.getItems().addAll(monsters);
                    System.out.println("allMonster: " + allMonster.size());
                }));
        return parent;
    }

    public void filterMonster(InputMethodEvent inputMethodEvent) {
    }

    public void backToPrevious(ActionEvent actionEvent) {
        app.showPrevious();
    }

    public void saveBeastTeam(ActionEvent actionEvent) {
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
