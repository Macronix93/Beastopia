package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Trainer;

import javax.inject.Inject;

public class MondexService {

    @Inject
    DataCache dataCache;
    @Inject
    PresetsService presetsService;

    @Inject
    MondexService() {
    }

    public void addKnownMonster(int id) {
        Trainer trainer = dataCache.getTrainer();
        if (!trainer.encounteredMonsterTypes().contains(id)) {
            trainer.encounteredMonsterTypes().add(id);
            dataCache.setTrainer(trainer);
            //ToDo Refresh Server Trainer
        }
    }

}
