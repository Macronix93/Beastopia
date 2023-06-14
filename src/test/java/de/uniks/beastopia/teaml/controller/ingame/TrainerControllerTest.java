package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Chunk;
import de.uniks.beastopia.teaml.rest.Layer;
import de.uniks.beastopia.teaml.rest.Map;
import de.uniks.beastopia.teaml.rest.MapObject;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Spawn;
import de.uniks.beastopia.teaml.rest.TileSetDescription;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.BACK_SPACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest extends ApplicationTest {
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @InjectMocks
    TrainerController trainerController;
    @Mock
    Provider<DeleteTrainerController> deleteTrainerControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    Provider<MenuController> menuControllerProvider;
    @Mock
    DataCache cache;
    @Mock
    PresetsService presetsService;
    @Mock
    TrainerService trainerService;
    @Mock
    TokenStorage tokenStorage;

    Image image = createImage(2, 2, List.of(new Color(255, 0, 255), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0)));
    List<Pair<String, Image>> allCharacters = List.of(
            new Pair<>("A.png", image),
            new Pair<>("B.png", image),
            new Pair<>("C.png", image)
    );
    List<String> characters = List.of("A.png", "B.png", "C.png");
    User user = new User(null, null, "ID", "123", "ONLINE", null, List.of());
    TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    List<HashMap<String, Double>> polygon = List.of(new HashMap<>() {{
        put("x", 0.0);
        put("y", 0.0);
    }});
    List<HashMap<String, String>> properties = List.of(new HashMap<>() {{
        put("value", "AREA_NAME");
    }});
    MapObject rectObject = new MapObject(50, 0, "AREA_NAME", properties, null, 0, "RECT", true, 50, 100, 100);
    MapObject polyObject = new MapObject(50, 0, "POLY", null, polygon, 0, "POLY", true, 50, 60, 60);
    Layer objectGroup = new Layer(null, List.of(rectObject, polyObject), null, 1, 20, 20, "objectgroup", true, 2, 2, 0, 0);
    Chunk chunk = new Chunk(List.of(0, 1, 2, 3), 2, 2, 0, 0);
    Layer tilelayer = new Layer(List.of(chunk), null, null, 1, 0, 0, "tilelayer", true, 2, 2, 0, 0);
    Map map = new Map(List.of(tileSetDescription), List.of(tilelayer, objectGroup), 2, 24, 4);
    Region region = new Region(null, null, "ID", "NAME", new Spawn(null, 0, 0), map);
    List<Trainer> allTrainer = List.of(
            new Trainer(null, null, "123", "A", "123", "A", "A.png", 0, null, 0, 0, 0, null),
            new Trainer(null, null, "456", "B", "456", "B", "B.png", 1, null, 0, 0, 0, null)
    );

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        trainerController.setRegion(region);

        when(cache.getTrainer()).thenReturn(allTrainer.get(0));
        when(cache.getCharacters()).thenReturn(allCharacters);
        when(cache.getCharacterImage(anyString())).thenReturn(allCharacters.get(0));
        when(presetsService.getCharacters()).thenReturn(Observable.just(characters));
        doNothing().when(mock(TrainerController.class)).showTrainerSpritePreview(any(), any());

        app.start(stage);
        app.show(trainerController);
        stage.requestFocus();
    }

    @Test
    void createNewTrainer() {
        TextField trainerNameInput = lookup("#trainerNameInput").query();
        trainerNameInput.setText(null);
        assertNull(trainerNameInput.getText());

        when(cache.getTrainer()).thenReturn(null);
        assertNull(cache.getTrainer());

        IngameController mockedIngameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(mockedIngameController);
        when(mockedIngameController.render()).thenReturn(new Button());

        when(trainerService.createTrainer(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(
                        new Trainer(null, null, "ID", "REGION", "USER", "TRAINER_NAME", "A.png", 0, null, 0, 0, 0, new NPCInfo(false))));

        clickOn("#trainerNameInput");
        write("MyTrainer");
        clickOn("#saveTrainerButton");

        verify(trainerService).createTrainer(anyString(), anyString(), anyString());
        verify(ingameControllerProvider).get();
        verify(mockedIngameController).render();
    }

    @Test
    void updateCurrentTrainer() {
        IngameController mockedIngameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(mockedIngameController);
        when(mockedIngameController.render()).thenReturn(new Button());

        when(trainerService.updateTrainer(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(
                        new Trainer(null, null, "ID", "REGION", "USER", "TRAINER_NAME", "B.png", 0, null, 0, 0, 0, new NPCInfo(false))));

        clickOn("#trainerNameInput");
        write("B");
        clickOn("#chooseRight");
        clickOn("#saveTrainerButton");

        verify(trainerService).updateTrainer(anyString(), anyString(), anyString(), anyString());
        verify(ingameControllerProvider).get();
        verify(mockedIngameController).render();
    }

    @Test
    void showTrainerDelete() {
        DeleteTrainerController mocked = mock();
        when(deleteTrainerControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Button());

        clickOn("#deleteTrainerButton");

        verify(deleteTrainerControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    void clickOnLeftArrow() {
        Text spriteName = lookup("#spriteNameDisplay").query();
        assertEquals("A", spriteName.getText());
        ImageView trainerSprite = lookup("#trainerSprite").query();
        assertEquals(allCharacters.get(0).getValue(), trainerSprite.getImage());

        clickOn("#chooseLeft");

        assertEquals("C", spriteName.getText());
        assertEquals(allCharacters.get(2).getValue(), trainerSprite.getImage());
    }

    @Test
    void clickOnRightArrow() {
        Text spriteName = lookup("#spriteNameDisplay").query();
        assertEquals("A", spriteName.getText());
        ImageView trainerSprite = lookup("#trainerSprite").query();
        assertEquals(allCharacters.get(0).getValue(), trainerSprite.getImage());

        clickOn("#chooseRight");

        assertEquals("B", spriteName.getText());
        assertEquals(allCharacters.get(1).getValue(), trainerSprite.getImage());
    }

    @Test
    void clickOnBackButtonMenu() {
        trainerController.backController("menu");

        MenuController mocked = mock();
        when(menuControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Button());

        clickOn("#backButton");

        verify(menuControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    void clickOnBackButtonIngame() {
        trainerController.backController("pause");

        IngameController mocked = mock();
        when(ingameControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Button());

        clickOn("#backButton");

        verify(ingameControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    void checkExistingTrainer() {
        when(cache.getCharacters()).thenReturn(Collections.emptyList());
        Assertions.assertTrue(cache.getCharacters().isEmpty());

        when(trainerService.getAllTrainer(anyString())).thenReturn(Observable.just(allTrainer));
        when(tokenStorage.getCurrentUser()).thenReturn(user);

        when(cache.getTrainer()).thenReturn(null);
        when(tokenStorage.getCurrentUser()).thenReturn(user);

        trainerController.init();
        trainerController.render();

        when(cache.getCharacters()).thenReturn(allCharacters);
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleTrainer"));
    }

    @Test
    void noTrainerNameEntered() {
        press(BACK_SPACE);
        release(BACK_SPACE);

        TextField trainerNameInput = lookup("#trainerNameInput").query();
        assertEquals("", trainerNameInput.getText());

        clickOn("#saveTrainerButton");

        Node dialogPane = lookup(".dialog-pane").query();
        Node result = from(dialogPane).lookup((Text t) -> t.getText().contains("a name for your trainer")).query();
        assertNotNull(result);
    }


    @SuppressWarnings("SameParameterValue")
    private static Image createImage(int width, int height, List<Color> colors) {
        // create buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            if (i >= colors.size()) {
                break;
            }
            for (int x = 0; x < image.getWidth(); x++) {
                if (i >= colors.size()) {
                    break;
                }
                image.setRGB(x, y, colors.get(i++).getRGB());
            }
        }
        return convertToFxImage(image);
    }

    // sauce: https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
}