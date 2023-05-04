package de.uniks.beastopia.teaml.rest;

import de.uniks.beastopia.teaml.model.Region;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

import java.util.List;

public interface RegionApiService {
    @GET("regions")
    Observable<List<Region>> getRegions();
}
