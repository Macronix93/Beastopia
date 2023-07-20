package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ItemDetailController extends Controller {

    @FXML
    public VBox VBoxItemDetail;
    @FXML
    public Label cost;
    @FXML
    public Label name;
    @FXML
    public ImageView itemImage;
    @FXML
    public Button shopBtn;
    @FXML
    public Label desc;
    @Inject
    PresetsService presetsService;
    private ItemTypeDto itemType;
    private boolean isShop;

    public ItemDetailController setItem(ItemTypeDto itemType) {
        this.itemType = itemType;
        return this;
    }

    public void setBooleanShop(boolean isShop) {
        this.isShop = isShop;
    }

    @Inject
    public ItemDetailController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (isShop) {
            shopBtn.setText(resources.getString("buy"));
        } else {
            shopBtn.setText(resources.getString("sell"));
        }

        name.setText(itemType.name());
        cost.setText(resources.getString("val") + ": " + itemType.price());
        desc.setText(formatStringIfTooLong(itemType.description()));
        disposables.add(presetsService.getItemImage(itemType.id())
                .observeOn(FX_SCHEDULER)
                .subscribe(itemImage -> this.itemImage.setImage(itemImage)));

        return parent;
    }

    private String formatStringIfTooLong(String itemName) { //If name is too long
        if (itemName.length() > 25) {
            int lastSpace = itemName.lastIndexOf(' ', 25);
            if (lastSpace != -1) { //\n after last space
                return itemName.substring(0, lastSpace) + "\n" + itemName.substring(lastSpace + 1);
            } else { //if too long and no space
                return itemName.substring(0, 25) + "-\n" + itemName.substring(25);
            }
        }
        return itemName;
    }

    @FXML
    public void shopFunction(ActionEvent actionEvent) {

    }
}
