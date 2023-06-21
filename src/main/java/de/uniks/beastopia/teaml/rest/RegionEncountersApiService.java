package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface RegionEncountersApiService {
    @GET("regions/{region_id}/encounters")
    Observable<List<Encounter>> getRegionEncounters(@Path("region_id") String region_id);

    @GET("regions/{region_id}/encounters/{id}")
    Observable<Encounter> getRegionEncounter(@Path("region_id") String region_id, @Path("id") String id);
}
