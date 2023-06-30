package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface RegionEncountersApiService {
    @GET("regions/{regionId}/encounters")
    Observable<List<Encounter>> getRegionEncounters(@Path("regionId") String region_id);

    @GET("regions/{regionId}/encounters/{id}")
    Observable<Encounter> getRegionEncounter(@Path("regionId") String region_id, @Path("id") String id);
}
