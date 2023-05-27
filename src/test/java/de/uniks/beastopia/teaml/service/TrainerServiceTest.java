package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.TrainerApiService;
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

    List<Trainer> allTrainer = List.of(new Trainer(null, null, "123", "A", "123", "A", "A", 0, null, 0, 0, 0, null),
            new Trainer(null, null, "456", "B", "456", "B", "B", 1, null, 0, 0, 0, null));

    @Test
    void createTrainerTest() {
        Trainer trainer = allTrainer.get(0);
        when(trainerApiService.createTrainer(new CreateTrainerDto("A", "A")))
                .thenReturn(Observable.just(trainer));

        Trainer responseTrainer = trainerService.createTrainer("A", "A").blockingFirst();

        assertEquals("123", responseTrainer._id());
        assertEquals("A", responseTrainer.region());
        assertEquals("123", responseTrainer.userId());
        assertEquals("A", responseTrainer.name());
        assertEquals("A", responseTrainer.image());
        assertEquals(0, responseTrainer.coins());
        verify(trainerApiService).createTrainer(new CreateTrainerDto("A", "A"));
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
        assertEquals("123", responseTrainer.userId());
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
        assertEquals("123", deletedtrainer.userId());
        assertEquals("A", deletedtrainer.name());
        assertEquals("A", deletedtrainer.image());
        assertEquals(0, deletedtrainer.coins());
        verify(trainerApiService).deleteTrainer("123", "123");
    }

}
