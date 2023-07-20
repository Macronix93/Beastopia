package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerItemsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class InventoryController extends Controller {

    private final Map<ItemTypeDto, Integer> itemMap = new HashMap<>();
    private final List<ItemController> subControllers = new ArrayList<>();
    @FXML
    public VBox VBoxInvList;
    @FXML
    public VBox VBoxItems;
    @FXML
    public Button CloseButton;
    @Inject
    TrainerItemsService trainerItemsService;
    @Inject
    PresetsService presetsService;
    @Inject
    DataCache cache;
    @Inject
    Provider<ItemController> itemControllerProvider;
    private Runnable onCloseRequest;
    private boolean isShop;
    private Consumer<ItemTypeDto> onItemClicked;

    @Inject
    public InventoryController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        if (this.isShop) {
            CloseButton.setDisable(true);
            CloseButton.setOpacity(0);
        }
        List<ItemTypeDto> itemTypes = presetsService.getItems().blockingFirst();
        disposables.add(trainerItemsService.getItems(cache.getJoinedRegion()._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER).subscribe(
                        i -> {
                            itemMap.clear();
                            for (Item item : i) {
                                itemMap.put(itemTypes.get(item.type()), item.amount());
                            }
                            reload();
                        }
                ));

        return parent;
    }

    private void reload() {
        for (ItemTypeDto itemTypeDto : itemMap.keySet()) {
            ItemController itemController = itemControllerProvider.get().setItem(itemTypeDto);
            itemController.setScore(itemMap.get(itemTypeDto));
            itemController.setOnItemClicked(onItemClicked);
            itemController.init();
            subControllers.add(itemController);
            Parent parent = itemController.render();
            VBoxItems.getChildren().add(parent);
        }
    }

    @Override
    public String getTitle() {
        if (this.isShop) {
            return resources.getString("titleInventory");
        } else {
            return resources.getString("titleShop");
        }
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    @Override
    public void destroy() {
        for (ItemController subController : subControllers) {
            subController.destroy();
        }
    }

    public void setIfShop(boolean b) {
        this.isShop = b;
    }
}
