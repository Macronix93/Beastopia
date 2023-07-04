package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface EncounterOpponentsApiService {
    @GET("regions/{regionId}/trainers/{trainer_id}/opponents")
    Observable<List<Opponent>> getTrainerOpponents(@Path("regionId") String regionId, @Path("trainer_id") String
            trainer_id);

    @GET("regions/{regionId}/encounters/{encounterId}/opponents")
    Observable<List<Opponent>> getEncounterOpponents(@Path("regionId") String regionId, @Path("encounterId") String
            encounterId);

    @GET("regions/{regionId}/encounters/{encounterId}/opponents/{opponent_id}")
    Observable<Opponent> getEncounterOpponent(@Path("regionId") String regionId, @Path("encounterId") String
            encounterId, @Path("opponent_id") String opponent_id);

    @PATCH("regions/{regionId}/encounters/{encounterId}/opponents/{opponent_id}")
    Observable<Opponent> updateEncounterOpponent(@Path("regionId") String regionId, @Path("encounterId") String
            encounterId, @Path("opponent_id") String opponent_id, @Body UpdateOpponentDto updateOpponentDto);

    @DELETE("regions/{regionId}/encounters/{encounterId}/opponents/{opponent_id}")
    Observable<Opponent> deleteOpponent(@Path("regionId") String regionId, @Path("encounterId") String
            encounter_id, @Path("opponent_id") String opponent_id);

}
