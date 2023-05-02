package de.uniks.beastopia.teaml.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class RegionController extends Controller {

    @FXML
    public VBox RegionList;
    @FXML
    public ScrollPane ScrollPane;

    @Inject
    public RegionController() {
    }

    public Parent render() {
        Parent parent = super.render();
        getRegions();
        return parent;

    }

    public Button createButton(String name) {
        Button button = new Button(name);
        button.setPrefWidth(RegionList.getPrefWidth());
        button.setPrefHeight(ScrollPane.getPrefHeight() / 4);
        button.setOnAction(event -> {
            //ToDo add join Region
        });
        return button;
    }

    public void getRegions() {
        //TODO get Regions from Server
        RegionList.getChildren().add(createButton("Region 1")); //for testing
    }
}
