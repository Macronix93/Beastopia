package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;
import java.util.function.Consumer;

public class ItemController extends Controller {
    @FXML
    public ImageView img;
    @FXML
    public Label name;
    @FXML
    public Label count;
    @Inject
    PresetsService presetsService;
    private Item item;
    private ItemTypeDto itemType = null;
    private Consumer<Item> onItemClicked;

    @Inject
    public ItemController() {

    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(presetsService.getItem(item.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(i -> itemType = i));

        name.setText(itemType.name());
        //TODO count
        disposables.add(presetsService.getItemImage(item.type())
                .observeOn(FX_SCHEDULER)
                .subscribe(itemImage -> img.setImage(itemImage)));

        return parent;
    }

    @FXML
    public void toggleDetails(MouseEvent mouseEvent) {
        onItemClicked.accept(item);
    }
    public ItemController setItem(Item item) {
        this.item = item;
        return this;
    }

    public void setOnItemClicked(Consumer<Item> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }
}
