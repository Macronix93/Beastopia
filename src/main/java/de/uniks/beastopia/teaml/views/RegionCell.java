package de.uniks.beastopia.teaml.views;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.Region;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ResourceBundle;

public class RegionCell extends ListCell<Region> {

    private final ResourceBundle resources;
    @Inject
    Provider<IngameController> ingameControllerProvider;

    @Inject
    App app;

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
                IngameController controller = ingameControllerProvider.get();
                controller.setRegion(item);
                app.show(controller);
            });
            setGraphic(join);
        }
        setAlignment(Pos.CENTER);
        getStyleClass().add("region-cell");
    }
}
