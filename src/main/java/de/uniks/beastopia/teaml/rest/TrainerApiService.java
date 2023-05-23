package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface TrainerApiService {
    @POST("regions/{regionId}/trainers")
    Observable<Trainer> createTrainer();

    @GET("regions/{regionId}/trainers")
    Observable<List<Trainer>> getAllTrainer();


}
