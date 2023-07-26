package de.uniks.beastopia.teaml.service;

import javax.inject.Inject;

public class MondexService {

    @Inject
    DataCache dataCache;
    @Inject
    PresetsService presetsService;

    @Inject
    MondexService() {
    }

    public boolean checkKnown(int id) {
        return dataCache.getTrainer().encounteredMonsterTypes().contains(id);
    }
}
