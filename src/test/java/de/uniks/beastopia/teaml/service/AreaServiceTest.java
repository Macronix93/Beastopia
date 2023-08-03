package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.AreaApiService;
import de.uniks.beastopia.teaml.rest.Chunk;
import de.uniks.beastopia.teaml.rest.Layer;
import de.uniks.beastopia.teaml.rest.Map;
import de.uniks.beastopia.teaml.rest.Position;
import de.uniks.beastopia.teaml.rest.TileSetDescription;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AreaServiceTest {
    @Mock
    AreaApiService areaApiService;
    @InjectMocks
    AreaService areaService;

    final TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    final Chunk chunk = new Chunk(List.of(0L, 1L, 2L, 3L), 2, 2, 0, 0);
    final Layer layer = new Layer(List.of(chunk), List.of(), null, null, 1, 0, 0, null, true, 2, 2, 0, 0);
    final Map map = new Map(List.of(tileSetDescription), List.of(layer), 2, 24, 4);
    final Area area = new Area(null, null, "ID_AREA", "ID_REGION", "AREA_NAME", new Position(0, 0), map);
    final List<Area> areas = List.of(area);

    @Test
    void getAreas() {
        when(areaApiService.getAreas(anyString())).thenReturn(Observable.just(areas));
        final List<Area> areaResults = areaService.getAreas("ID_REGION").blockingFirst();

        // check values:
        assertNull(areaResults.get(0).createdAt());
        assertNull(areaResults.get(0).updatedAt());
        assertEquals("ID_AREA", areaResults.get(0)._id());
        assertEquals("ID_REGION", areaResults.get(0).region());
        assertEquals("AREA_NAME", areaResults.get(0).name());
        assertEquals(new Position(0, 0), areaResults.get(0).spawn());
        assertEquals(map, areaResults.get(0).map());

        // check mocks
        verify(areaApiService).getAreas("ID_REGION");
    }

    @Test
    void getArea() {
        when(areaApiService.getArea(anyString(), anyString())).thenReturn(Observable.just(area));
        final Area areaResult = areaService.getArea("ID_REGION", "ID_AREA").blockingFirst();

        // check values:
        assertNull(areaResult.createdAt());
        assertNull(areaResult.updatedAt());
        assertEquals("ID_AREA", areaResult._id());
        assertEquals("ID_REGION", areaResult.region());
        assertEquals("AREA_NAME", areaResult.name());
        assertEquals(new Position(0, 0), areaResult.spawn());
        assertEquals(map, areaResult.map());

        // check mocks
        verify(areaApiService).getArea("ID_REGION", "ID_AREA");
    }
}
