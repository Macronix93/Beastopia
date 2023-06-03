package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface TrainerApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> createTrainer(@Path("regionId") String regionId, @Body CreateTrainerDto createTrainerDto);

    @GET("regions/{regionId}/trainers")
    Observable<List<Trainer>> getAllTrainer(@Path("regionId") String regionId);

    @GET("regions/{regionId}/trainers/{id}")
    Observable<Trainer> getTrainer(@Path("regionId") String regionId, @Path("id") String trainerId);

    @DELETE("regions/{regionId}/trainers/{id}")
    Observable<Trainer> deleteTrainer(@Path("regionId") String regionId, @Path("id") String trainerId);

    @PATCH("regions/{regionId}/trainers/{id}")
    Observable<Trainer> updateTrainer(@Path("regionId") String regionId, @Path("id") String trainerId, @Body UpdateTrainerDto updateTrainerDto);
}