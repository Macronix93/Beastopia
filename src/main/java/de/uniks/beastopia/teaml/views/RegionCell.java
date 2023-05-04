package de.uniks.beastopia.teaml.views;

import de.uniks.beastopia.teaml.model.Region;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;

public class RegionCell extends ListCell<Region> {
    @Override
    protected void updateItem(Region item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
        } else {
            final Button join = new Button("Join");
            join.setOnAction(event -> {
                System.out.println("Joining region " + item.getName());
                //TODO join region
            });
            setText(item.getName());
        }
    }
}
