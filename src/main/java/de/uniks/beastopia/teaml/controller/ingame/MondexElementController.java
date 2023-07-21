package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class MondexElementController extends Controller{

    @Inject
    PresetsService presetsService;
    public ImageView imageView_avatar;
    public Label label_name;
    public Label label_id;
    private MonsterTypeDto monster;


    @Inject
    public MondexElementController() {

    }

    public MondexElementController setMonster(MonsterTypeDto monsterTypeDto) {
        this.monster = monsterTypeDto;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        label_name.setText(monster.name());
        label_id.setText("#" + (monster.id()));

        disposables.add(presetsService.getMonsterImage(monster.id())
                .observeOn(FX_SCHEDULER)
                .subscribe(monsterImage -> imageView_avatar.setImage(monsterImage)));

        return parent;
    }
}
