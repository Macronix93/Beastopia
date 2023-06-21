package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

import javax.inject.Inject;

public class EditBeastTeamController extends Controller {

    @FXML
    public ListView<Monster> beastList;
    @FXML
    public TextField filterBar;
    @FXML
    public ListView<Monster> beastTeam;
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

    ObservableList<Monster> allBeast = FXCollections.observableArrayList();
    ObservableList<Monster> team = FXCollections.observableArrayList();

    @Inject
    public EditBeastTeamController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public String getTitle() {
        return resources.getString("TitelBeastTeam");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    this.allBeast.addAll(monsters);
                    this.beastList.setItems(allBeast);
                    this.beastTeam.setItems(team);
                    this.beastList.setCellFactory(param -> new BeastListElementController());
                    this.beastTeam.setCellFactory(param -> new BeastListElementController());
                    filterBar.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.isEmpty()) {
                            beastList.setItems(allBeast);
                        } else {
                            ObservableList<Monster> filtered = FXCollections.observableArrayList();
                            for (Monster monster : allBeast) {
                                if (monster.level() == Integer.parseInt(newValue)) {
                                    filtered.add(monster);
                                }
                            }
                            beastList.setItems(filtered);
                        }
                    });
                }));
        return parent;
    }

    public void filterMonster(InputMethodEvent inputMethodEvent) {
    }

    public void backToPrevious() {
        app.showPrevious();
    }

    public void saveBeastTeam() {
        team.add(allBeast.remove(0));
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
