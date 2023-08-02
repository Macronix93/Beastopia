package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopControllerTest extends ApplicationTest {

    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    final Runnable onCloseRequest = mock();
    final Trainer trainer = new Trainer(null, null, "id", "region", "user", "name", "image", List.of("team"), List.of(), List.of("visitedAreas"), 0, "area", 0, 0, 0, new NPCInfo(true, false, false, false, List.of(1), List.of(), null));
    private final List<ItemTypeDto> itemTypeDtos = List.of(new ItemTypeDto(1, "img", "name", 32, "desc", "use"));
    @Spy
    App app;
    @InjectMocks
    ShopController shopController;
    @Mock
    Provider<ItemController> itemControllerProvider;
    @Mock
    ItemController mockedItemController;
    @Mock
    PresetsService presetsService;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        shopController.setTrainer(trainer);
        when(presetsService.getItems()).thenReturn(Observable.just(itemTypeDtos));
        when(itemControllerProvider.get()).thenReturn(mockedItemController);
        when(mockedItemController.setItem(any())).thenReturn(mockedItemController);
        doNothing().when(mockedItemController).setOnItemClicked(any());
        doNothing().when(mockedItemController).init();
        when(mockedItemController.render()).thenReturn(new VBox());
        app.start(stage);
        app.show(shopController);
        stage.requestFocus();
    }

    @Test
    public void renderTest() {
        VBox items = lookup("#VBoxItems").queryAs(VBox.class);
        assertEquals(1, items.getChildren().size());
    }

    @Test
    void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleShop"));
    }

    @Test
    public void closeTest() {
        clickOn("#CloseButtonTest");
        verify(onCloseRequest, times(1)).run();
    }
}
