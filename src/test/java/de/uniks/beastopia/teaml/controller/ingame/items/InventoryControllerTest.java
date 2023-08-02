package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerItemsService;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest extends ApplicationTest {

    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    final Runnable onCloseRequest = mock();
    final Consumer<ItemTypeDto> onItemClicked = mock();
    final Trainer trainer = new Trainer(null, null, "id", "region", "user", "name", "image", List.of("team"), List.of(), List.of("visitedAreas"), 10, "area", 0, 0, 0, new NPCInfo(true, false, false, false, List.of(1), List.of(), null));
    final Region region = new Region(null, null, "id", "name", null, null);
    private final List<ItemTypeDto> itemTypeDtos = List.of(new ItemTypeDto(0, "img", "name", 32, "desc", "use"));
    private final List<Item> items = List.of(new Item(null, "name", "desc", "use", 0, 3));
    @Spy
    App app;
    @InjectMocks
    InventoryController inventoryController;
    @Mock
    Provider<ItemController> itemControllerProvider;
    @Mock
    ItemController itemController;
    @Mock
    DataCache cache;
    @Mock
    PresetsService presetsService;
    @Mock
    TrainerItemsService trainerItemsService;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        when(presetsService.getItems()).thenReturn(Observable.just(itemTypeDtos));
        when(cache.getTrainer()).thenReturn(trainer);
        when(cache.getJoinedRegion()).thenReturn(region);
        when(trainerItemsService.getItems(anyString(), anyString())).thenReturn(Observable.just(items));
        when(itemController.setItem(any())).thenReturn(itemController);
        when(itemControllerProvider.get()).thenReturn(itemController);
        when(itemController.setItem(any())).thenReturn(itemController);
        doNothing().when(itemController).setOnItemClicked(any());
        doNothing().when(itemController).init();
        when(itemController.render()).thenReturn(new VBox());
        inventoryController.setIfShop(false);
        inventoryController.setOnCloseRequest(onCloseRequest);
        app.start(stage);
        app.show(inventoryController);
        stage.requestFocus();
        Platform.runLater(() -> {
            inventoryController.render();
            inventoryController.setOnItemClicked(onItemClicked);
        });
    }

    @Test
    public void getTitleTest() {
        inventoryController.setIfShop(false);
        assertEquals(resources.getString("titleInventory"), inventoryController.getTitle());

        inventoryController.setIfShop(true);
        assertEquals(resources.getString("titleShop"), inventoryController.getTitle());
    }

    @Test
    public void closeTest() {
        clickOn("#CloseButton");
        verify(onCloseRequest, times(1)).run();
    }
}