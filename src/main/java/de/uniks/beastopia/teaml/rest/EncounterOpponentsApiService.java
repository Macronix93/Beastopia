package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface EncounterOpponentsApiService {
    @GET("regions/{region_id}/trainers/{trainer_id}/opponents")
    Observable<List<Opponent>> getTrainerOpponents(@Path("region_id") String region_id, @Path("trainer_id") String
            trainer_id);

    @GET("regions/{region_id}/encounters/{encounter_id}/opponents")
    Observable<List<Opponent>> getEncounterOpponents(@Path("region_id") String region_id, @Path("id") String
            encounter_id);

    @GET("regions/{region_id}/encounters/{encounter_id}/opponents/{opponent_id}")
    Observable<Opponent> getEncounterOpponent(@Path("region_id") String region_id, @Path("encounter_id") String
            encounter_id, @Path("opponent_id") String opponent_id);

    @PATCH("regions/{region_id}/encounters/{encounter_id}/opponents/{opponent_id}")
    Observable<Opponent> updateEncounterOpponent(@Path("region_id") String region_id, @Path("encounter_id") String
            encounter_id, @Path("opponent_id") String opponent_id, @Body UpdateOpponentDto updateOpponentDto);

    @DELETE("regions/{region_id}/encounters/{encounter_id}/opponents/{opponent_id}")
    Observable<Opponent> deleteOpponent(@Path("region_id") String region_id, @Path("encounter_id") String
            encounter_id, @Path("opponent_id") String opponent_id);

}
