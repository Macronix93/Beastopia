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

    public Observable<List<Encounter>> getRegionEncounters(String regionId) {
        return regionEncountersApiService.getRegionEncounters(regionId);
    }

    public Observable<Encounter> getRegionEncounter(String regionId, String encounterId) {
        return regionEncountersApiService.getRegionEncounter(regionId, encounterId);
    }
}
