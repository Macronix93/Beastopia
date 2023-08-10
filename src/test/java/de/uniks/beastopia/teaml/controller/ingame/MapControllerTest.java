package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
    Provider<RegionInfoController> regionInfoControllerProvider;
    @SuppressWarnings("unused")

    @Mock
    PresetsService presetsService;
    @Mock
    RegionInfoController regionInfoController;

    final TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    final Chunk chunk = new Chunk(List.of(0L, 1L, 2L, 3L), 2, 2, 0, 0);
    final Layer tilelayer = new Layer(List.of(chunk), List.of(), null, null, 1, 0, 0, "tilelayer", true, 2, 2, 0, 0);
    final Trainer trainer = new Trainer(null, null, "1", "A", "1", "A", null, null, List.of(), List.of(), 0, "1", 0, 0, 0, null);
    final List<HashMap<String, Double>> polygon = List.of(new HashMap<>() {{
        put("x", 0.0);
        put("y", 0.0);
    }});
    final List<HashMap<String, String>> properties = List.of(new HashMap<>() {{
        put("value", "AREA_NAME");
    }});
    final MapObject rectObject = new MapObject(50, 0, "AREA_NAME", properties, null, 0, "RECT", true, 50, 100, 100);
    final MapObject polyObject = new MapObject(50, 0, "POLY", null, polygon, 0, "POLY", true, 50, 60, 60);
    final Layer objectgroup = new Layer(null, List.of(), List.of(rectObject, polyObject), null, 1, 20, 20, "objectgroup", true, 2, 2, 0, 0);
    final Map map = new Map(List.of(tileSetDescription), List.of(tilelayer, objectgroup), 2, 24, 4);
    final Area area = new Area(null, null, "ID_AREA", "ID_REGION", "AREA_NAME", new Position(0, 0), map);
    final Region region = new Region(null, null, "ID_REGION", "REGION_NAME", null, map);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        when(cache.getTrainer()).thenReturn(trainer);
        when(cache.getArea(any())).thenReturn(area);
        when(cache.getJoinedRegion()).thenReturn(region);
        when(regionInfoControllerProvider.get()).thenReturn(regionInfoController);
        //doNothing().when(regionInfoController).init();

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
        assertEquals(7, mapController.anchorPane.getChildren().size());
    }

    @Test
    public void regionInfoTest() {
        when(regionInfoController.render()).thenReturn(new Label());
        int size = mapController.anchorPane.getChildren().size();
        Node node = mapController.anchorPane.getChildren().get(size - 2);
        clickOn(node);
        assertEquals(8, mapController.anchorPane.getChildren().size());
        moveTo(app.getStage().getScene().getRoot(), Point2D.ZERO);
    }
}