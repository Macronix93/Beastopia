package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.model.Region;
import de.uniks.beastopia.teaml.rest.RegionApiService;
import de.uniks.beastopia.teaml.views.RegionCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class RegionController extends Controller {

    @FXML
    public VBox RegionList;
    @FXML
    public ScrollPane ScrollPane;

    RegionApiService regionApiService;

    private final ObservableList<Region> regions = FXCollections.observableArrayList();

    @Inject
    public RegionController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        final ListView<Region> regions = new ListView<>(this.regions);
        regions.setCellFactory(param -> new RegionCell());

        //RegionList.getChildren().clear();
        //getRegions();

        return new VBox(regions);
    }

    @Override
    public void init() {
        super.init();
        /*disposables.add(RegionApiService.getRegions().subscribe(regions -> {
            this.regions.setAll(regions);
        }));*/
    }

    public void addButton(String name) {
        Button button = new Button(name);
        button.setPrefWidth(RegionList.getPrefWidth());
        button.setPrefHeight(ScrollPane.getPrefHeight() / 4);
        button.setOnAction(event -> {
            //ToDo add join Region
        });
        RegionList.getChildren().add(button);
    }

    public void getRegions() {
        //TODO get Regions from Server
        addButton("Region 1");
        addButton("Region 2");
    }
}
