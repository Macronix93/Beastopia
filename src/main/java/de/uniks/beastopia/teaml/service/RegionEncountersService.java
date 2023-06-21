package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.RegionEncountersApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class RegionEncountersService {

    @Inject
    RegionEncountersApiService regionEncountersApiService;

    @Inject
    public RegionEncountersService() {
    }

    public Observable<List<Encounter>> getRegionEncounters(String region_id) {
        return regionEncountersApiService.getRegionEncounters(region_id);
    }

    public Observable<Encounter> getRegionEncounter(String region_id, String id) {
        return regionEncountersApiService.getRegionEncounter(region_id, id);
    }
}
