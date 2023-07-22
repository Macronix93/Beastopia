package de.uniks.beastopia.teaml.controller.ingame.mondex;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.service.ImageService;
import de.uniks.beastopia.teaml.service.PresetsService;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
    private Consumer<MonsterTypeDto> onBeastClicked;

    @Inject
    public MondexElementController() {

    }

    public void setOnBeastClicked(Consumer<MonsterTypeDto> onBeastClicked) {
        this.onBeastClicked = onBeastClicked;
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
        //ToDo Change if to known
        if (monster.id() % 2 == 0) {
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

    @FXML
    public void toggleDetails() {
        onBeastClicked.accept(monster);
    }

}
