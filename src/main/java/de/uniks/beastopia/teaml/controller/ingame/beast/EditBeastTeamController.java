package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
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
    ObservableList<Monster> beastList = FXCollections.observableArrayList();
    ObservableList<Monster> teamList = FXCollections.observableArrayList();
    List<MonsterTypeDto> allBeasts;

    @Inject
    public EditBeastTeamController() {
    }

    @Override
    public void init() {
        disposables.add(presetsService.getAllBeasts()
                .observeOn(FX_SCHEDULER)
                .subscribe(
                        beasts -> {
                            cache.setAllBeasts(beasts);
                            allBeasts = beasts;
                        },
                        error -> System.out.println("Error: " + error)
                ));
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
                    setupListView(monsters);
                    this.beastListView.setCellFactory(param -> beastListElementControllerProvider.get());
                    this.teamListView.setCellFactory(param -> beastListElementControllerProvider.get());
                }));
        return parent;
    }

    public void setupListView(List<Monster> monsters) {
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
        ObservableList<Monster> filtered;
        if (isInt(filterBar.getText())) {
            filtered = beastList.filtered(monster -> monster.level() == Integer.parseInt(filterBar.getText()));
        } else {
            List<MonsterTypeDto> matchingNames = allBeasts.stream()
                    .filter(monsterTypeDto ->
                            monsterTypeDto.name().toLowerCase().startsWith(filterBar.getText().toLowerCase()))
                    .toList();

            filtered = beastList.filtered(monster
                    -> matchingNames.stream().anyMatch(monsterTypeDto -> monster.type() == monsterTypeDto.id()));
        }
        beastListView.setItems(filtered);
    }

    public void moveItemToBeasts() {
        filterBar.clear();
        beastListView.setItems(beastList);
        if (teamList.size() > 0) {
            beastList.add(teamListView.getSelectionModel().getSelectedItem());
            teamListView.getItems().remove(teamListView.getSelectionModel().getSelectedItem());
        }
    }

    public void moveItemToTeam() {
        filterBar.clear();
        beastListView.setItems(beastList);
        if (teamList.size() < 6) {
            teamList.add(beastListView.getSelectionModel().getSelectedItem());
            beastListView.getItems().remove(beastListView.getSelectionModel().getSelectedItem());
        }
    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}