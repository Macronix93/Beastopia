package de.uniks.beastopia.teaml.controller.ingame.beast;

import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BeastListElementControllerTest extends ApplicationTest {

    @InjectMocks
    BeastListElementController beastListElementController;
    ListView<Monster> monsterListView;
    MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    Monster monster1 = new Monster(null, null, "MONSTER_1", "TRAINER_ID", 0, 1, 1, attributes, currentAttributes);
    Monster monster2 = new Monster(null, null, "MONSTER_2", "TRAINER_ID", 0, 2, 5, attributes, currentAttributes);
    Monster monster3 = new Monster(null, null, "MONSTER_3", "TRAINER_ID", 0, 3, 2, attributes, currentAttributes);


    @Override
    public void start(Stage stage) throws Exception {
        FxToolkit.setupFixture(() -> {
            monsterListView = new ListView<>();
            monsterListView.getItems().addAll(monster1);
            monsterListView.setCellFactory(param -> beastListElementController);
        });
        FxToolkit.showStage();
    }

    @Test
    public void updateItem() {
        assertEquals(3, monsterListView.getItems().size());
        sleep(2000);
    }
}