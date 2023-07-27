package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerItemsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class InventoryController extends Controller {

    @FXML
    public VBox VBoxInvList;
    @FXML
    public VBox VBoxItems;
    @FXML
    public Button CloseButton;
    @FXML
    public Label inv;
    @FXML
    public ImageView coinImg;
    @Inject
    TrainerItemsService trainerItemsService;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;
    @Inject
    Provider<ItemController> itemControllerProvider;
    private Runnable onCloseRequest;
    public boolean isShop;
    private Consumer<ItemTypeDto> onItemClicked;
    private final List<ItemTypeDto> itemTrainerTypes = new ArrayList<>();
    private List<ItemTypeDto> presetItemTypes = new ArrayList<>();
    private List<Item> trainerItems = new ArrayList<>();
    private final List<ItemController> subControllers = new ArrayList<>();

    @Inject
    public InventoryController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        coinImg.setImage(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/coin.png"))));
        showTrainerCoins();
        if (this.isShop) {
            CloseButton.setDisable(true);
            CloseButton.setOpacity(0);
        }
        disposables.add(presetsService.getItems()
                .observeOn(FX_SCHEDULER)
                .subscribe(items -> {
                    presetItemTypes = items;
                    reload();
                }));

        return parent;
    }

    public void showTrainerCoins() {
        inv.setText(resources.getString("inv") + "\t" + cache.getTrainer().coins());
    }

    public void updateInventory() {
        subControllers.forEach(Controller::destroy);
        subControllers.clear();
        VBoxItems.getChildren().clear();
        showTrainerCoins();
        reload();
    }

    private void reload() {
        disposables.add(trainerItemsService.getItems(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(itemList -> {
                    trainerItems = itemList;
                    System.out.println(trainerItems.toString());
                    itemTrainerTypes.clear();
                    for (ItemTypeDto itemType : presetItemTypes) { //filter items
                        for (Item item : trainerItems) {
                            if (itemType.id() == item.type() && item.amount() > 0) {
                                itemTrainerTypes.add(itemType);
                            }
                        }
                    }
                    for (ItemTypeDto itemTypeDto : itemTrainerTypes) { //create subController
                        ItemController itemController = itemControllerProvider.get().setItem(itemTypeDto);
                        itemController.setScore(Objects.requireNonNull(findItem(itemTypeDto)).amount());
                        itemController.setOnItemClicked(onItemClicked);
                        itemController.init();
                        subControllers.add(itemController);
                        Parent parent = itemController.render();
                        VBoxItems.getChildren().add(parent);
                    }
                }));
    }

    private Item findItem(ItemTypeDto itemTypeDto) {
        for (Item item : trainerItems) {
            if (item.type() == itemTypeDto.id()) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getTitle() {
        if (this.isShop) {
            return resources.getString("titleShop");
        } else {
            return resources.getString("titleInventory");
        }
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
        if (!isShop) {
            if (keyEvent.getCode().equals(KeyCode.B)) {
                onCloseRequest.run();
            }
        }
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    public void setOnItemClicked(Consumer<ItemTypeDto> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    public void setIfShop(boolean b) {
        this.isShop = b;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (ItemController subController : subControllers) {
            subController.destroy();
        }
    }
}
