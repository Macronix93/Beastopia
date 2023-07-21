package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
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
    public ItemTypeDto itemType;
    public int score;
    private Consumer<ItemTypeDto> onItemClicked;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;

    @Inject
    public ItemController() {

    }

    public ItemController setItem(ItemTypeDto itemType) {
        this.itemType = itemType;
        return this;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setOnItemClicked(Consumer<ItemTypeDto> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (score != 0) {
            count.setText("x" + score);
        }

        name.setText(formatStringIfTooLong(itemType.name()));

        if (cache.getItemImages().containsKey(itemType.id())) {
            img.setImage(cache.getItemImages().get(itemType.id()));
        } else {
            disposables.add(presetsService.getItemImage(itemType.id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(itemImage -> {
                        img.setImage(itemImage);
                        Map<Integer, Image> itemImages = new HashMap<>();
                        itemImages.put(itemType.id(), itemImage);
                        cache.setItemImages(itemImages);
                    }));
        }

        return parent;
    }

    public String formatStringIfTooLong(String itemName) {
        if (itemName.length() > 12) {
            int lastSpace = itemName.lastIndexOf(' ', 12);
            if (lastSpace != -1) { // \n after last space
                if (itemName.length() > 24) {
                    String lastPart = itemName.substring(lastSpace + 1);
                    return itemName.substring(0, lastSpace) + "\n" + formatStringIfTooLong(lastPart);
                }
                return itemName.substring(0, lastSpace) + "\n" + formatStringIfTooLong(itemName.substring(lastSpace + 1));
            } else { // if too long and no space
                if (itemName.length() > 24) {
                    String lastPart = itemName.substring(12);
                    return itemName.substring(0, 12) + "-\n" + formatStringIfTooLong(lastPart);
                }
                return itemName.substring(0, 12) + "-\n" + itemName.substring(12);
            }
        }
        return itemName;
    }

    @FXML
    public void toggleDetails() {
        onItemClicked.accept(itemType);
    }
}
