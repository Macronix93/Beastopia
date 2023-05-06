package de.uniks.beastopia.teaml.controller;

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

public class RegionController extends Controller {

    private final ObservableList<Region> regions = FXCollections.observableArrayList();
    @FXML
    public VBox regionList;
    @FXML
    public ScrollPane scrollPane;
    @Inject
    RegionService regionService;

    @Inject
    public RegionController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        final ListView<Region> regions = new ListView<>(this.regions);
        regions.setCellFactory(param -> new RegionCell());
        regionList.getChildren().add(regions);
        return parent;
    }

    @Override
    public void init() {
        super.init();
        disposables.add(regionService.getRegions().subscribe(this.regions::setAll));
    }
}
