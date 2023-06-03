package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateTrainerDto;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.TrainerApiService;
import de.uniks.beastopia.teaml.rest.UpdateTrainerDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainerService {
    @Inject
    TrainerApiService trainerApiService;

    @Inject
    TrainerService() {
    }

    public Observable<Trainer> createTrainer(String regionId, String name, String image) {
        return trainerApiService.createTrainer(regionId, new CreateTrainerDto(name, image));
    }

    public Observable<List<Trainer>> getAllTrainer(String regionId) {
        return trainerApiService.getAllTrainer(regionId);
    }

    public Observable<Trainer> getTrainer(String regionId, String trainerId) {
        return trainerApiService.getTrainer(regionId, trainerId);
    }

    public Observable<Trainer> deleteTrainer(String regionId, String trainerId) {
        return trainerApiService.deleteTrainer(regionId, trainerId);
    }

    public Observable<Trainer> updateTrainer(String regionId, String trainerId, String name, String image) {
        return trainerApiService.updateTrainer(regionId, trainerId, new UpdateTrainerDto(name, image));
    }
}