package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

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

    @GET("regions/{regionId}/trainers/{trainerId}/monsters")
    Observable<List<Monster>> getTrainerMonsters(@Path("regionId") String regionId, @Path("trainerId") String trainerId);

    @GET("regions/{regionId}/trainers/{trainerId}/monsters/{id}")
    Observable<Monster> getTrainerMonster(@Path("regionId") String regionId, @Path("trainerId") String trainerId, @Path("id") String id);
}