package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.AreaApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class AreaService {

    @Inject
    AreaApiService areaApiService;

    @Inject
    public AreaService() {
    }

    public Observable<List<Area>> getAreas(String region) {
        return areaApiService.getAreas(region);
    }

}
