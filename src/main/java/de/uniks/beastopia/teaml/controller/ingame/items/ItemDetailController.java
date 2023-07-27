package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
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
import java.util.List;
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
                cost.setText(resources.getString("val") + ": " + (int) (itemType.price() * 0.5));
            }
        } else {
            shopBtn.setDisable(true);
            shopBtn.setOpacity(0);
            if (itemType.price() == 0) {
                cost.setText(resources.getString("val") + ": " + itemType.price());
            } else {
                cost.setText(resources.getString("val") + ": " + (int) (itemType.price() * 0.5));
            }
        }
        name.setText(itemType.name());
        desc.setText(FormatString.formatString(itemType.description(), 25));
        itemImage.setImage(cache.getItemImages().get(itemType.id()));

        return parent;
    }

    @FXML
    public void shopFunction() {
        List<Item> items = cache.getItems();
        if (!buy) { //sell
            System.out.println("sell:" + itemType.id());
            disposables.add(trainerItemsService.updateItem(cache.getJoinedRegion()._id(), cache.getTrainer()._id(),
                    new UpdateItemDto(-1, itemType.id(), null)).observeOn(FX_SCHEDULER).subscribe(
                    itemUpdated -> {
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).type() == itemType.id()) {
                                items.set(i, itemUpdated);
                                cache.setItems(items);
                                break;
                            }
                        }
                    }, error -> System.out.println("Error:" + error)));
        } else { //buy
            System.out.println("buy: " + itemType.id());
            disposables.add(trainerItemsService.updateItem(cache.getJoinedRegion()._id(), cache.getTrainer()._id(),
                    new UpdateItemDto(1, itemType.id(), null)).observeOn(FX_SCHEDULER).subscribe(
                    itemUpdated -> {
                        for (int i = 0; i < items.size(); i++) {
                            if (items.get(i).type() == itemType.id()) {
                                items.set(i, itemUpdated);
                                cache.setItems(items);
                                break;
                            }
                        }
                    }, error -> System.out.println("Error:" + error)));
        }
        disposables.add(trainerService.getTrainer(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER).subscribe(t -> {
                    cache.setTrainer(t);
                    //inventar close und dann openshopinv in ingame?
                    this.inventoryController.updateInventory();
                    this.inventoryController.showTrainerCoins();
                    super.destroy();
                }));
    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
}
