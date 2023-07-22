package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {

    @Mock
    TrainerApiService trainerApiService;

    @InjectMocks
    TrainerService trainerService;

    final List<Trainer> allTrainer = List.of(new Trainer(null, null, "123", "A", "123", "A", "A", null, List.of(), List.of(), 0, null, 0, 0, 0, null),
            new Trainer(null, null, "456", "B", "456", "B", "B", null, List.of(), List.of(), 1, null, 0, 0, 0, null));

    final List<Monster> monsters = List.of(new Monster(null, null, "ID", "trainer",
            1, 1, 1, null, null, null));
    @Test
    void createTrainerTest() {
        Trainer trainer = allTrainer.get(0);
        when(trainerApiService.createTrainer("123", new CreateTrainerDto("A", "A")))
                .thenReturn(Observable.just(trainer));

        Trainer responseTrainer = trainerService.createTrainer("123", "A", "A").blockingFirst();

        assertEquals("123", responseTrainer._id());
        assertEquals("A", responseTrainer.region());
        assertEquals("123", responseTrainer.user());
        assertEquals("A", responseTrainer.name());
        assertEquals("A", responseTrainer.image());
        assertEquals(0, responseTrainer.coins());
        verify(trainerApiService).createTrainer("123", new CreateTrainerDto("A", "A"));
    }

    @Test
    void getAllTrainerTest() {
        when(trainerApiService.getAllTrainer("123")).thenReturn(Observable.just(allTrainer));

        List<Trainer> responseList = trainerService.getAllTrainer("123").blockingFirst();

        assertEquals("123", responseList.get(0)._id());
        assertEquals("A", responseList.get(0).region());
        assertEquals("456", responseList.get(1)._id());
        assertEquals("B", responseList.get(1).region());
        verify(trainerApiService).getAllTrainer("123");
    }

    @Test
    void getTrainerTest() {
        when(trainerApiService.getTrainer("123", "123")).thenReturn(Observable.just(allTrainer.get(0)));

        Trainer responseTrainer = trainerService.getTrainer("123", "123").blockingFirst();

        assertEquals("123", responseTrainer._id());
        assertEquals("A", responseTrainer.region());
        assertEquals("123", responseTrainer.user());
        assertEquals("A", responseTrainer.name());
        assertEquals("A", responseTrainer.image());
        assertEquals(0, responseTrainer.coins());
        verify(trainerApiService).getTrainer("123", "123");
    }

    @Test
    void deleteTrainerTest() {
        when(trainerApiService.deleteTrainer("123", "123")).thenReturn(Observable.just(allTrainer.get(0)));

        Trainer deletedtrainer = trainerService.deleteTrainer("123", "123").blockingFirst();

        assertEquals("123", deletedtrainer._id());
        assertEquals("A", deletedtrainer.region());
        assertEquals("123", deletedtrainer.user());
        assertEquals("A", deletedtrainer.name());
        assertEquals("A", deletedtrainer.image());
        assertEquals(0, deletedtrainer.coins());
        verify(trainerApiService).deleteTrainer("123", "123");
    }

    @Test
    void updateTrainer() {
        when(trainerApiService.updateTrainer("region", "trainer",
                new UpdateTrainerDto("A", "A", List.of(), null)))
                .thenReturn(Observable.just(allTrainer.get(0)));

        Trainer result = trainerService.updateTrainer("region", "trainer", "A", "A", List.of())
                .blockingFirst();

        assertEquals("A", result.name());
        assertEquals("A", result.image());

        verify(trainerApiService).updateTrainer("region", "trainer",
                new UpdateTrainerDto("A", "A", List.of(), null));
    }

    @Test
    void getTrainerMonsters() {
        when(trainerApiService.getTrainerMonsters("region", "trainer"))
                .thenReturn(Observable.just(monsters));
        List<Monster> result = trainerService.getTrainerMonsters("region", "trainer").blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("trainer", result.get(0).trainer());
        assertEquals(1, result.get(0).type());
        verify(trainerApiService).getTrainerMonsters("region", "trainer");
    }

    @Test
    void getTrainerMonster() {
        when(trainerApiService.getTrainerMonster("region", "trainer", "monster"))
                .thenReturn(Observable.just(monsters.get(0)));
        Monster result = trainerService.getTrainerMonster("region", "trainer", "monster").blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("trainer", result.trainer());
        assertEquals(1, result.type());
        verify(trainerApiService).getTrainerMonster("region", "trainer", "monster");
    }
}
