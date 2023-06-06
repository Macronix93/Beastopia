package de.uniks.beastopia.teaml.views;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.ingame.MapController;
import de.uniks.beastopia.teaml.controller.ingame.TrainerController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

public class RegionCell extends ListCell<Region> {

    private final ResourceBundle resources;
    @Inject
    Provider<TrainerController> trainerControllerProvider;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    DataCache cache;
    @Inject
    App app;

    //TODO: remove bypass
    @Inject
    Provider<MapController> mapControllerProvider;

    @Inject
    public RegionCell(ResourceBundle resources) {
        this.resources = resources;
    }

    @Override
    protected void updateItem(Region item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            final Button join = new Button(item.name() + " " + resources.getString("join"));
            join.setOnAction(event -> {
                TrainerController controller = trainerControllerProvider.get();
                cache.setRegion(item);
                controller.setRegion(item);
                controller.backController("menu");

                //TODO: remove bypass
                MapController mapController = mapControllerProvider.get();

                app.show(mapController);
            });
            setGraphic(join);
        }
        setAlignment(Pos.CENTER);
        getStyleClass().add("region-cell");
    }
}
