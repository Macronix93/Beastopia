package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.util.function.Consumer;

public class ItemController extends Controller {
    @FXML
    public ImageView img;
    @FXML
    public Label name;
    @FXML
    public Label count;
    @FXML
    public GridPane _ItemRootElement;
    private ItemTypeDto itemType;
    private Consumer<ItemTypeDto> onItemClicked;
    @Inject
    PresetsService presetsService;

    @Inject
    public ItemController() {

    }

    public ItemController setItem(ItemTypeDto itemType) {
        this.itemType = itemType;
        return this;
    }

    public void setOnItemClicked(Consumer<ItemTypeDto> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        name.setText(itemType.name());
        //TODO count
        disposables.add(presetsService.getItemImage(itemType.id())
                .observeOn(FX_SCHEDULER)
                .subscribe(itemImage -> img.setImage(itemImage)));

        return parent;
    }

    @FXML
    public void toggleDetails(MouseEvent mouseEvent) {
        onItemClicked.accept(itemType);
    }
}
