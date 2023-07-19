package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerItemsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopController extends Controller {

    @FXML
    public VBox VBoxItems;
    @FXML
    public VBox VBoxShopList;
    @Inject
    DataCache cache;
    @Inject
    TrainerItemsService trainerItemsService;
    @Inject
    Provider<ItemController> itemControllerProvider;
    private Trainer seller;
    private final List<Item> sellerItems = new ArrayList<>();
    private final List<Controller> subControllers = new ArrayList<>();
    private Consumer<Item> onItemClicked;
    private Runnable onCloseRequest;

    @Inject
    public ShopController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleShop");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        disposables.add(trainerItemsService.getItems(cache.getJoinedRegion()._id(), seller._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(items -> {
                    this.sellerItems.addAll(items);
                    reload();
                }));

        return parent;
    }

    private void reload() {
        for (Item item : sellerItems) {
            ItemController itemController = itemControllerProvider.get().setItem(item);
            itemController.setOnItemClicked(onItemClicked);
            itemController.init();
            subControllers.add(itemController);
            Parent parent = itemController.render();
            VBoxItems.getChildren().add(parent);
        }
    }

    public void setTrainer(Trainer trainer) {
        this.seller = trainer;
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        onCloseRequest.run();
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
    }

    @Override
    public void destroy() {
        for (Controller subController : subControllers) {
            subController.destroy();
        }
    }

    public void setOnCloseRequest(Runnable onCloseRequest) {
        this.onCloseRequest = onCloseRequest;
    }
}
