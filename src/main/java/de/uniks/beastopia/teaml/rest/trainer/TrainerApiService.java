package de.uniks.beastopia.teaml.rest.trainer;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface TrainerApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> createTrainer(@Body CreateTrainerDto createTrainerDto);

    @GET("regions/{regionId}/trainers")
    Observable<List<Trainer>> getAllTrainer(@Path("regionId") String regionId);

    @GET("regions/{regionId}/trainers/{id}")
    Observable<Trainer> getTrainer(@Path("regionId") String regionId, @Path("id") String trainerId);

    @DELETE("regions/{regionId}/trainers/{id}")
    Observable<Trainer> deleteTrainer(@Path("regionId") String regionId, @Path("id") String trainerId);
}
