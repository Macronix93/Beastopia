package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.RegionEncountersApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegionEncountersServiceTest {
    @Mock
    RegionEncountersApiService regionEncountersApiService;
    @InjectMocks
    RegionEncountersService regionEncountersService;

    @Test
    void getRegionEncounters() {
        when(regionEncountersApiService.getRegionEncounters(anyString()))
                .thenReturn(Observable.just(List.of(new Encounter(null, null, "ID", "REGION", true))));
        List<Encounter> result = regionEncountersService.getRegionEncounters("ID").blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("REGION", result.get(0).region());
        verify(regionEncountersApiService).getRegionEncounters("ID");
    }

    @Test
    void getRegionEncounter() {
        when(regionEncountersApiService.getRegionEncounter(anyString(), anyString()))
                .thenReturn(Observable.just(new Encounter(null, null, "ID", "REGION", true)));
        Encounter result = regionEncountersService.getRegionEncounter("ID", "ID").blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("REGION", result.region());
        assertTrue(result.isWild());
        verify(regionEncountersApiService).getRegionEncounter("ID", "ID");
    }
}
