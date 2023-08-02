package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Trainer;

import javax.inject.Inject;

public class MondexService {

    @Inject
    DataCache dataCache;
    @Inject
    PresetsService presetsService;

    Trainer trainer;

    @Inject
    MondexService() {
    }

    public void init() {
        trainer = dataCache.getTrainer();
    }

    public boolean checkKnown(int id) {
        return trainer.encounteredMonsterTypes().contains(id);
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }
}
