package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.RegionApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {
    @Mock
    RegionApiService regionApiService;
    @InjectMocks
    RegionService regionService;

    @Test
    void getRegions() {
        when(regionApiService.getRegions())
                .thenReturn(Observable.just(List.of(new Region(null, null, "ID", "NAME", null))));
        List<Region> result = regionService.getRegions().blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("NAME", result.get(0).name());
        verify(regionApiService).getRegions();
    }
}