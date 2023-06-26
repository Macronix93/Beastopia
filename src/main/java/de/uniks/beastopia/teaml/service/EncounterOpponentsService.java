package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
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

    public Observable<List<Opponent>> getTrainerOpponents(String region_id, String trainer_id) {
        return encounterOpponentsApiService.getTrainerOpponents(region_id, trainer_id);
    }

    public Observable<List<Opponent>> getEncounterOpponents(String region_id, String encounter_id) {
        return encounterOpponentsApiService.getEncounterOpponents(region_id, encounter_id);
    }

    public Observable<Opponent> getEncounterOpponent(String region_id, String encounter_id, String opponent_id) {
        return encounterOpponentsApiService.getEncounterOpponent(region_id, encounter_id, opponent_id);
    }

    @SuppressWarnings("SameParameterValue")
    Observable<Opponent> updateEncounterOpponent(String region_id, String encounter_id, String opponent_id, String monster, AbilityMove abilityMove) {
        Variant<AbilityMove, ChangeMonsterMove> move = new Variant<>();
        move.setT(abilityMove);
        return encounterOpponentsApiService.updateEncounterOpponent(region_id, encounter_id, opponent_id,
                new UpdateOpponentDto(monster, move));
    }

    @SuppressWarnings("SameParameterValue")
    Observable<Opponent> updateEncounterOpponent(String region_id, String encounter_id, String opponent_id, String monster, ChangeMonsterMove changeMonsterMove) {
        Variant<AbilityMove, ChangeMonsterMove> move = new Variant<>();
        move.setU(changeMonsterMove);
        return encounterOpponentsApiService.updateEncounterOpponent(region_id, encounter_id, opponent_id,
                new UpdateOpponentDto(monster, move));
    }

    public Observable<Opponent> deleteOpponent(String region_id, String encounter_id, String opponent_id) {
        return encounterOpponentsApiService.deleteOpponent(region_id, encounter_id, opponent_id);
    }
}
