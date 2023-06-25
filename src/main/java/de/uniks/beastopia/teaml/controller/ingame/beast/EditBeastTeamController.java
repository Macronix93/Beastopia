package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;

public class EditBeastTeamController extends Controller {

    @FXML
    public ListView<Monster> beastListView;
    @FXML
    public ListView<Monster> teamListView;
    @FXML
    public TextField filterBar;
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
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<BeastListElementController> beastListElementControllerProvider;

    MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    Monster monster1 = new Monster(null, null, "MONSTER_1", "TRAINER_ID", 0, 1, 1, attributes, currentAttributes);
    Monster monster2 = new Monster(null, null, "MONSTER_2", "TRAINER_ID", 0, 2, 5, attributes, currentAttributes);
    Monster monster3 = new Monster(null, null, "MONSTER_3", "TRAINER_ID", 0, 3, 2, attributes, currentAttributes);

    ObservableList<Monster> beastList = FXCollections.observableArrayList();
    ObservableList<Monster> teamList = FXCollections.observableArrayList();

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
        //TODO clean up
        disposables.add(trainerService.getTrainerMonsters(prefs.getRegionID(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsters -> {
                    Label label = new Label();
                    label.setText(resources.getString("NoBeasts"));
                    beastListView.setPlaceholder(label);
                    teamListView.setPlaceholder(label);
                    List<String> prefTeam = prefs.getBeastTeam();
                    for (Monster monster : monsters) {
                        if (prefTeam.contains(monster._id())) {
                            teamList.add(monster);
                        } else {
                            beastList.add(monster);
                        }
                    }
                    this.beastListView.setItems(beastList);
                    this.teamListView.setItems(teamList);
                    this.beastListView.setCellFactory(param -> beastListElementControllerProvider.get());
                    this.teamListView.setCellFactory(param -> beastListElementControllerProvider.get());
                    // TODO remove
                    /*filterBar.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.isEmpty()) {
                            beastListView.setItems(beastList);
                        } else {
                            ObservableList<Monster> filtered = FXCollections.observableArrayList();
                            for (Monster monster : beastList) {
                                if (monster.level() == Integer.parseInt(newValue)) {
                                    filtered.add(monster);
                                }
                            }
                            beastListView.setItems(filtered);
                        }
                    });*/
                }));
        return parent;
    }

    public void backToPrevious() {
        app.showPrevious();
    }

    public void saveBeastTeam() {
        if (!teamList.isEmpty()) {
            List<String> beastTeam = teamList.stream().map(Monster::_id).toList();
            prefs.setBeastTeam(beastTeam);
        } else {
            prefs.removeBeastTeam();
        }
        app.showPrevious();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void filterMonster() {
        //TODO add filter functionality here instead of in render()
        System.out.println("Filter");
    }

    public void moveItemToBeasts() {
        if (teamList.size() > 0) {
            beastList.add(teamListView.getSelectionModel().getSelectedItem());
            teamListView.getItems().remove(teamListView.getSelectionModel().getSelectedItem());
        }
    }

    public void moveItemToTeam() {
        if (teamList.size() < 6) {
            teamList.add(beastListView.getSelectionModel().getSelectedItem());
            beastListView.getItems().remove(beastListView.getSelectionModel().getSelectedItem());
        }
    }

    public void addFake() {
        beastList.addAll(List.of(monster1, monster2, monster3));
    }
}
