package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.EncounterOpponentsApiService;
import de.uniks.beastopia.teaml.rest.Opponent;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class EncounterOpponentsServiceTest {
    @Mock
    EncounterOpponentsApiService encounterOpponentsApiService;
    @InjectMocks
    EncounterOpponentsService encounterOpponentsService;

    final List<Opponent> opponents = List.of(new Opponent(null, null, "ID", "Encounter",
            "Trainer", true, true, null, null, null, 0));

    @Test
    void getTrainerOpponents() {
        when(encounterOpponentsApiService.getTrainerOpponents(anyString(), anyString()))
                .thenReturn(Observable.just(opponents));
        List<Opponent> result = encounterOpponentsService.getTrainerOpponents("ID", "ID").blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("Encounter", result.get(0).encounter());
        assertEquals("Trainer", result.get(0).trainer());
        assertTrue(result.get(0).isAttacker());
        assertTrue(result.get(0).isNPC());
        verify(encounterOpponentsApiService).getTrainerOpponents("ID", "ID");
    }

    @Test
    void getEncounterOpponents() {
        when(encounterOpponentsApiService.getEncounterOpponents(anyString(), anyString()))
                .thenReturn(Observable.just(opponents));
        List<Opponent> result = encounterOpponentsService.getEncounterOpponents("ID", "ID").blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("Encounter", result.get(0).encounter());
        assertEquals("Trainer", result.get(0).trainer());
        assertTrue(result.get(0).isAttacker());
        assertTrue(result.get(0).isNPC());
        verify(encounterOpponentsApiService).getEncounterOpponents("ID", "ID");
    }

    @Test
    void getEncounterOpponent() {
        when(encounterOpponentsApiService.getEncounterOpponent(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(opponents.get(0)));
        Opponent result = encounterOpponentsService.getEncounterOpponent("ID", "ID", "ID").blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("Encounter", result.encounter());
        assertEquals("Trainer", result.trainer());
        assertTrue(result.isAttacker());
        assertTrue(result.isNPC());
        verify(encounterOpponentsApiService).getEncounterOpponent("ID", "ID", "ID");
    }

    @Test
    void deleteOpponent() {
        when(encounterOpponentsApiService.deleteOpponent(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(opponents.get(0)));
        Opponent result = encounterOpponentsService.deleteOpponent("ID", "ID", "ID").blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("Encounter", result.encounter());
        assertEquals("Trainer", result.trainer());
        assertTrue(result.isAttacker());
        assertTrue(result.isNPC());
        verify(encounterOpponentsApiService).deleteOpponent("ID", "ID", "ID");
    }
}
