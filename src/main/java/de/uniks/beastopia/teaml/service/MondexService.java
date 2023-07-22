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

    public void addKnownMonster(int id) {
        Trainer trainer = dataCache.getTrainer();
        if (!trainer.encounteredMonsterTypes().contains(id)) {
            trainer.encounteredMonsterTypes().add(id);
            dataCache.setTrainer(trainer);
            //ToDo Refresh Server Trainer
        }
    }

    public boolean checkKnown(int id) {
        return dataCache.getTrainer().encounteredMonsterTypes().contains(id);
    }


    public void listMonsterTypes(List<MonsterTypeDto> monsterTypeDtos) {
        List <String> monsterTypes = new ArrayList<>();
        for (MonsterTypeDto monsterTypeDto : monsterTypeDtos) {
                if (monsterTypeDto.type().size() == 1) {
                    if (!monsterTypes.contains(monsterTypeDto.type().get(0))) {
                        monsterTypes.add(monsterTypeDto.type().get(0));
                    }
            } else {
                if (!monsterTypes.contains(monsterTypeDto.type().get(0))) {
                    monsterTypes.add(monsterTypeDto.type().get(0));
                }
                if (!monsterTypes.contains(monsterTypeDto.type().get(1))) {
                    monsterTypes.add(monsterTypeDto.type().get(1));
                }
            }
        }
        System.out.println("All Monster Types: " + monsterTypes);
    }
}
