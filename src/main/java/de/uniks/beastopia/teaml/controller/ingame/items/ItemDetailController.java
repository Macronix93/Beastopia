package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.IngameController;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.rest.UpdateItemDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerItemsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.FormatString;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Objects;

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
    @FXML
    public ImageView coinImg;
    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    TrainerItemsService trainerItemsService;
    private ItemTypeDto itemType;
    private boolean isShop;
    private boolean onlyInventory;
    private InventoryController inventoryController;
    private boolean buy;
    private IngameController ingameController;

    public void setItem(ItemTypeDto itemType) {
        this.itemType = itemType;
    }

    public void setBooleanShop(boolean isShop) {
        this.isShop = isShop;
    }

    public void setOnlyInventory(boolean onlyInventory) {
        this.onlyInventory = onlyInventory;
    }

    @Inject
    public ItemDetailController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        coinImg.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/coin.png"))));
        if (isShop && !onlyInventory) {
            if (itemType.price() > cache.getTrainer().coins()) {
                shopBtn.setDisable(true);
            }
            shopBtn.setText(resources.getString("buy"));
            cost.setText(resources.getString("val") + ": " + itemType.price());
            buy = true;
        } else if (!onlyInventory) {
            shopBtn.setText(resources.getString("sell"));
            buy = false;
            if (itemType.price() == 0) {
                cost.setText(resources.getString("val") + ": " + itemType.price());
                shopBtn.setOpacity(0);
                shopBtn.setDisable(true);
            } else {
                float price = (float) (itemType.price() * 0.5);
                String formattedPrice = String.format("%.1f", price);
                cost.setText(resources.getString("val") + ": " + formattedPrice);
            }
        } else {
            shopBtn.setDisable(true);
            shopBtn.setOpacity(0);
            if (itemType.price() == 0) {
                cost.setText(resources.getString("val") + ": " + itemType.price());
            } else {
                float price = (float) (itemType.price() * 0.5);
                String formattedPrice = String.format("%.1f", price);
                cost.setText(resources.getString("val") + ": " + formattedPrice);
            }
        }
        name.setText(itemType.name());
        desc.setText(FormatString.formatString(itemType.description(), 25));
        itemImage.setImage(cache.getItemImages().get(itemType.id()));

        return parent;
    }

    @FXML
    public void shopFunction() {
        int amount = 1; //buy
        if (!buy) { //sell
            amount = -1;
        }
        disposables.add(trainerItemsService.updateItem(cache.getJoinedRegion()._id(), cache.getTrainer()._id(),
                new UpdateItemDto(amount, itemType.id(), null)).observeOn(FX_SCHEDULER).subscribe(
                itemUpdated -> {}, error -> System.out.println("Error:" + error)));
        disposables.add(trainerService.getTrainer(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER).subscribe(trainer -> {
                    cache.setTrainer(trainer);
                    inventoryController.updateInventory();
                }, error -> System.out.println("Error:" + error)));
        ingameController.toggleInventoryItemDetails(itemType);
    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    public void setIngameController(IngameController ingameController) {
        this.ingameController = ingameController;
    }
}
