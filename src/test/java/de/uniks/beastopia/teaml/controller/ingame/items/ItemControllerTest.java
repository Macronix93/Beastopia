package de.uniks.beastopia.teaml.controller.ingame.items;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.ItemTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest extends ApplicationTest {

    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @InjectMocks
    ItemController itemController;
    @Mock
    PresetsService presetsService;
    final ItemTypeDto itemTypeDto = new ItemTypeDto(1, "img", "name", 32, "desc", "use");
    Image expectedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/de/uniks/beastopia/teaml/assets/group.png")));

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);
        itemController.setItem(itemTypeDto);
        itemController.setScore(100);
        when(presetsService.getItemImage(1)).thenReturn(Observable.just(expectedImage));
        app.start(stage);
        app.show(itemController);
        stage.requestFocus();
    }

    @Test
    void test() {
        assertEquals("name", lookup("#name").queryAs(javafx.scene.control.Label.class).getText());
        assertEquals("x100", lookup("#count").queryAs(javafx.scene.control.Label.class).getText());
        assertEquals(expectedImage, lookup("#img").queryAs(ImageView.class).getImage());
    }

    @Test
    void testFormatStringIfTooLong() {
        // name < 12 characters
        String shortName = "Name";
        assertEquals(shortName, itemController.formatStringIfTooLong(shortName));

        // name > 12 characters with space
        String longNameWithSpaces = "This is a long name";
        String expectedFormattedNameWithSpaces = "This is a\nlong name";
        assertEquals(expectedFormattedNameWithSpaces, itemController.formatStringIfTooLong(longNameWithSpaces));

        // name > 12 characters without space
        String longNameWithoutSpaces = "ThisIsALongName";
        String expectedFormattedNameWithoutSpaces = "ThisIsALongN-\name";
        assertEquals(expectedFormattedNameWithoutSpaces, itemController.formatStringIfTooLong(longNameWithoutSpaces));

        // name > 24 characters without space
        String longLongNameWithoutSpaces = "ThisIsALongNameWithoutSpaces";
        String expectedLongFormattedNameWithoutSpaces = "ThisIsALongN-\nameWithoutSp-\naces";
        assertEquals(expectedLongFormattedNameWithoutSpaces, itemController.formatStringIfTooLong(longLongNameWithoutSpaces));
    }
}
