package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.service.RegionService;
import de.uniks.beastopia.teaml.views.RegionCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class RegionController extends Controller {

    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    @FXML
    public VBox regionList;
    @FXML
    public ScrollPane scrollPane;
    @Inject
    RegionService regionService;
    @Inject
    Provider<RegionCell> regionCellProvider;

    @Inject
    public RegionController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        final ListView<Region> regions = new ListView<>(this.regions);
        regions.setCellFactory(param -> regionCellProvider.get());
        regionList.getChildren().add(regions);
        VBox.setVgrow(regions, javafx.scene.layout.Priority.ALWAYS);
        disposables.add(regionService.getRegions().subscribe(this.regions::setAll));
        return parent;
    }
}
