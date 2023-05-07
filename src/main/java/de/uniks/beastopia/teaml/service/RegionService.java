package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.RegionApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionService {

    private final RegionApiService regionApiService;

    @Inject
    public RegionService(RegionApiService regionApiService) {
        this.regionApiService = regionApiService;
    }

    public Observable<List<Region>> getRegions() {
        return regionApiService.getRegions();
    }
}
