package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.RegionService;
import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapControllerTest extends ApplicationTest {


    @Spy
    App app;
    @Spy
    final ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    MapController mapController;
    @Mock
    DataCache cache;
    @Mock
    PresetsService presetsService;
    @Mock
    RegionService regionService;
    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    Provider<RegionInfoController> regionInfoControllerProvider;
    @Mock
    RegionInfoController regionInfoController;

    final TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    final TileSet tileSet = new TileSet(2, "IMAGE", 2, 2, 0, "NAME", 0, 4, 1);
    final Chunk chunk = new Chunk(List.of(0, 1, 2, 3), 2, 2, 0, 0);
    final Layer tilelayer = new Layer(List.of(chunk), null, null, 1, 0, 0, "tilelayer", true, 2, 2, 0, 0);
    final Trainer trainer = new Trainer(null, null, "1", "A", "1", "A", null, 0, "1", 0, 0, 0, null);
    final Image image = createImage(2, 2, List.of(new Color(255, 0, 255), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0)));
    final List<HashMap<String, Double>> polygon = List.of(new HashMap<>() {{
        put("x", 0.0);
        put("y", 0.0);
    }});
    final List<HashMap<String, String>> properties = List.of(new HashMap<>() {{
        put("value", "AREA_NAME");
    }});
    final MapObject rectObject = new MapObject(50, 0, "AREA_NAME", properties, null, 0, "RECT", true, 50, 100, 100);
    final MapObject polyObject = new MapObject(50, 0, "POLY", null, polygon, 0, "POLY", true, 50, 60, 60);
    final Layer objectgroup = new Layer(null, List.of(rectObject, polyObject), null, 1, 20, 20, "objectgroup", true, 2, 2, 0, 0);
    final Map map = new Map(List.of(tileSetDescription), List.of(tilelayer, objectgroup), 2, 24, 4);
    final Area area = new Area(null, null, "ID_AREA", "ID_REGION", "AREA_NAME", map);
    final Region region = new Region(null, null, "ID_REGION", "REGION_NAME", null, map);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        when(cache.getTrainer()).thenReturn(trainer);
        when(cache.getArea(any())).thenReturn(area);
        when(cache.getJoinedRegion()).thenReturn(region);
        when(presetsService.getTileset(any())).thenReturn(Observable.just(tileSet));
        when(presetsService.getImage(any())).thenReturn(Observable.just(image));
        when(regionService.getRegion(any())).thenReturn(Observable.just(region));
        when(regionInfoControllerProvider.get()).thenReturn(regionInfoController);
        doNothing().when(regionInfoController).init();

        app.start(stage);
        app.show(mapController);
        stage.requestFocus();
    }

    @Test
    public void titleTest() {
        assertEquals(resources.getString("TitleMap"), mapController.getTitle());
    }

    @Test
    public void drawTest() {
        assertEquals(8, mapController.anchorPane.getChildren().size());
    }

    @Test
    public void regionInfoTest() {
        when(regionInfoController.render()).thenReturn(new Label());
        moveTo(app.getStage().getScene().getRoot(), new Point2D(-200, -160));
        assertEquals(9, mapController.anchorPane.getChildren().size());
        moveTo(app.getStage().getScene().getRoot(), Point2D.ZERO);
        assertEquals(8, mapController.anchorPane.getChildren().size());
    }


    @Test
    void closeMapTest() {
        final IngameController mock = Mockito.mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(mock);
        doNothing().when(mock).setRegion(any());
        when(mock.render()).thenReturn(new Label());

        clickOn("#closeMapButton");

        verify(mock).render();
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