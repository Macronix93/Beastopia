package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.ChangeMonsterMove;
import de.uniks.beastopia.teaml.rest.EncounterOpponentsApiService;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.rest.UpdateOpponentDto;
import de.uniks.beastopia.teaml.utils.Variant;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class EncounterOpponentsService {
    @Inject
    EncounterOpponentsApiService encounterOpponentsApiService;

    @Inject
    public EncounterOpponentsService() {
    }

    public Observable<List<Opponent>> getTrainerOpponents(String regionId, String trainerId) {
        return encounterOpponentsApiService.getTrainerOpponents(regionId, trainerId);
    }

    public Observable<List<Opponent>> getEncounterOpponents(String regionId, String encounterId) {
        return encounterOpponentsApiService.getEncounterOpponents(regionId, encounterId);
    }

    public Observable<Opponent> getEncounterOpponent(String regionId, String encounterId, String opponentId) {
        return encounterOpponentsApiService.getEncounterOpponent(regionId, encounterId, opponentId);
    }

    @SuppressWarnings("SameParameterValue")
    public Observable<Opponent> updateEncounterOpponent(String regionId, String encounterId, String opponentId, String monster, AbilityMove abilityMove) {
        Variant<AbilityMove, ChangeMonsterMove> move = new Variant<>();
        move.setT(abilityMove);
        return encounterOpponentsApiService.updateEncounterOpponent(regionId, encounterId, opponentId,
                new UpdateOpponentDto(monster, move));
    }

    @SuppressWarnings("SameParameterValue")
    public Observable<Opponent> updateEncounterOpponent(String regionId, String encounterId, String opponentId, String monster, ChangeMonsterMove changeMonsterMove) {
        Variant<AbilityMove, ChangeMonsterMove> move = new Variant<>();
        move.setU(changeMonsterMove);
        return encounterOpponentsApiService.updateEncounterOpponent(regionId, encounterId, opponentId,
                new UpdateOpponentDto(monster, move));
    }

    public Observable<Opponent> deleteOpponent(String region_id, String encounter_id, String opponent_id) {
        return encounterOpponentsApiService.deleteOpponent(region_id, encounter_id, opponent_id);
    }
}
