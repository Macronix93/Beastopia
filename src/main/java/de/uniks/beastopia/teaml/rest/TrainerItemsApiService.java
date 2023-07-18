package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface TrainerItemsApiService {

    @POST("regions/{regionId}/trainers/{trainerId}/items")
    Observable<Item> updateItem(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Body UpdateItemDto updateItemDto);

    @GET("regions/{regionId}/trainers/{trainerId}/items")
    Observable<List<Item>> getItems(@Path("regionId") String regionId, @Path("trainerId") String trainerId);

    @GET("regions/{regionId}/trainers/{trainerId}/items/{ItemId}")
    Observable<Item> getItem(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Path("ItemId") String ItemId);
}
