package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.RegionApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionService {

    @Inject
    RegionApiService regionApiService;

    @Inject
    public RegionService() {
    }

    public Observable<List<Region>> getRegions() {
        return regionApiService.getRegions();
    }

    public Observable<Region> getRegion(String id) {
        return regionApiService.getRegion(id);
    }
}
