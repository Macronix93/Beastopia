package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface AreaApiService {
    @GET("regions/{region}/areas")
    Observable<List<Area>> getAreas(@Path("region") String region);

    @SuppressWarnings("unused")
    @GET("regions/{region}/areas/{id}")
    Observable<Area> getArea(@Path("region") String region, @Path("id") String id);
}
