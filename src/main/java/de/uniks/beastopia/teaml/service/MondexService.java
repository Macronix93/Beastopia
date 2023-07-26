package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.rest.Trainer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
