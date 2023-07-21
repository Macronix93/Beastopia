package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.ResourceBundle;

public class MondexElementController extends Controller {
    public ImageView imageView_avatar;
    public Label label_name;
    public Label label_id;
    @Inject
    ResourceBundle resourceBundle;
    @Inject
    PresetsService presetsService;
    @Inject
    ImageService imageService;
    private MonsterTypeDto monster;
    private boolean known;

    @Inject
    public MondexElementController() {

    }

    public MondexElementController setMonster(MonsterTypeDto monsterTypeDto, boolean known) {
        this.monster = monsterTypeDto;
        this.known = known;
        return this;
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        label_id.setText("#" + (monster.id()));
        if (known) {
            label_name.setText(monster.name());
            disposables.add(presetsService.getMonsterImage(monster.id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> imageView_avatar.setImage(monsterImage)));
        } else {
            label_name.setText(resources.getString("Unknown"));
            disposables.add(presetsService.getMonsterImage(monster.id())
                    .observeOn(FX_SCHEDULER)
                    .subscribe(monsterImage -> imageView_avatar.setImage(imageService.makeImageBlack(monsterImage))));
        }

        return parent;
    }

}
