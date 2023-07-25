package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.FormatString;
import javafx.event.ActionEvent;
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
    private ItemTypeDto itemType;
    private boolean isShop;
    private boolean onlyInventory;

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
            shopBtn.setText(resources.getString("buy"));
            cost.setText(resources.getString("val") + ": " + itemType.price());
        } else if (!onlyInventory){
            shopBtn.setText(resources.getString("sell"));
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

    @SuppressWarnings("unused")
    @FXML
    public void shopFunction(ActionEvent actionEvent) {
        //TODO: Implement shop function
    }
}
