package de.uniks.beastopia.teaml.controller.ingame.mondex;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class MondexDetailController extends Controller {

    @FXML
    public VBox VBoxMondexDetail;
    @FXML
    public Label label_name;
    @FXML
    public ImageView imageView_TypeIcon;
    @FXML
    public Label label_type;
    @FXML
    public ImageView imageView_Avatar;
    @FXML
    public TextArea textArea_description;

    private MonsterTypeDto monster;


    @Inject
    public MondexDetailController() {

    }

    public MondexDetailController setMonster(MonsterTypeDto monster) {
        this.monster = monster;
        return this;
    }

    @Override
    public void init() {
        super.init();

    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        label_name.setText(monster.name());
        label_type.setText("Type:" + monster.type());
        return parent;
    }


}
