package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopController extends Controller {

    @FXML
    public VBox VBoxShopList;
    @FXML
    public VBox VBoxItems;
    @FXML
    public Button CloseButtonTest;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<ItemController> itemControllerProvider;
    private Runnable onCloseRequest;
    private Trainer seller;
    private final List<ItemTypeDto> sellerItems = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    private Consumer<ItemTypeDto> onItemClicked;

    public void setTrainer(Trainer trainer) {
        this.seller = trainer;
    }

    @Inject
    public ShopController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(presetsService.getItems()
                .observeOn(FX_SCHEDULER)
                .subscribe(items -> {
                    for (ItemTypeDto itemType : items) {
                        if (seller.npc().sells().contains(itemType.id())) {
                            sellerItems.add(itemType);
                        }
                    }
                    reload();
                }));
        return parent;
    }

    public void reload() {
        for (ItemTypeDto item : sellerItems) {
            ItemController itemController = itemControllerProvider.get().setItem(item);
            itemController.setOnItemClicked(onItemClicked);
            itemController.init();
            subControllers.add(itemController);
            Parent parent = itemController.render();
            VBoxItems.getChildren().add(parent);
        }
    }

    @Override
    public String getTitle() {
        return resources.getString("titleShop");
    }

    @FXML
    public void close() {
        onCloseRequest.run();
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }

    @SuppressWarnings("unused")
    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
    }

    public void setOnItemClicked(Consumer<ItemTypeDto> onItemClicked) {
        this.onItemClicked = onItemClicked;
    }

    @Override
    public void destroy() {
        for (Controller subController : subControllers) {
            subController.destroy();
        }
    }
}
