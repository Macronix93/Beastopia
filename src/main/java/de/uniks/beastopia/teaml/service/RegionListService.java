package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.RegionApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionListService {

    @Inject
    RegionApiService regionApiService;

    @Inject
    public RegionListService() {
    }

    public Observable<List<Region>> getRegions() {
        return regionApiService.getRegions();
    }
}
