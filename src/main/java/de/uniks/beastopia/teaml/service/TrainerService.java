package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.trainer.CreateTrainerDto;
import de.uniks.beastopia.teaml.rest.trainer.Trainer;
import de.uniks.beastopia.teaml.rest.trainer.TrainerApiService;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainerService {
    @Inject
    TokenStorage tokenStorage;

    @Inject
    TrainerApiService trainerApiService;

    @Inject
    TrainerService() {
    }

    public Observable<Trainer> createTrainer(String name, String image) {
        return trainerApiService.createTrainer(new CreateTrainerDto(name, image));
    }

    public Observable<List<Trainer>> getAllTrainer(String regionId) {
        return trainerApiService.getAllTrainer(regionId);
    }

    public Observable<Trainer> getTrainer(String regionId, String trainerId) {
        return trainerApiService.getTrainer(regionId, trainerId);
    }

    public Observable<Trainer> deleteTrainer(String regionId, String trainerId){
        return trainerApiService.deleteTrainer(regionId, trainerId);
    }
}
