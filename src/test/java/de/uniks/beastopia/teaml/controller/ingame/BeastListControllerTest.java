package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeastListControllerTest extends ApplicationTest {

    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @InjectMocks
    BeastListController beastListController;
    @Mock
    Provider<BeastController> beastControllerProvider;
    @Mock
    DataCache cache;
    @Mock
    TrainerService trainerService;
    @Mock
    Prefs prefs;

    MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    MonsterAttributes currentAttributes = new MonsterAttributes(0, 0, 0, 0);
    Monster monster1 = new Monster(null, null, "MONSTER_1", "TRAINER_ID", 0, 0, 0, null, attributes, currentAttributes);
    Monster monster2 = new Monster(null, null, "MONSTER_2", "TRAINER_ID", 0, 0, 0, null, attributes, currentAttributes);
    Monster monster3 = new Monster(null, null, "MONSTER_3", "TRAINER_ID", 0, 0, 0, null, attributes, currentAttributes);
    List<Monster> monsters = List.of(monster1, monster2, monster3);
    final BeastController mockedBeastController1 = mock();
    final BeastController mockedBeastController2 = mock();
    final BeastController mockedBeastController3 = mock();
    final Runnable onCloseRequest = mock();

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        Label testLabel1 = new javafx.scene.control.Label("Label1");
        testLabel1.setId("testLabelID1");
        Label testLabel2 = new javafx.scene.control.Label("Label2");
        testLabel2.setId("testLabelID2");
        Label testLabel3 = new Label("Label3");
        testLabel3.setId("testLabelID3");

        AtomicInteger callGet = new AtomicInteger(0);
        when(beastControllerProvider.get()).thenAnswer(invocation -> {
            if (callGet.get() == 0) {
                callGet.set(1);
                return mockedBeastController1;
            } else if (callGet.get() == 1) {
                callGet.set(2);
                return mockedBeastController2;
            } else {
                callGet.set(0);
                return mockedBeastController3;
            }
        });

        when(mockedBeastController1.setBeast(any())).thenReturn(mockedBeastController1);
        when(mockedBeastController2.setBeast(any())).thenReturn(mockedBeastController2);
        when(mockedBeastController3.setBeast(any())).thenReturn(mockedBeastController3);

        when(mockedBeastController1.render()).thenReturn(testLabel1);
        when(mockedBeastController2.render()).thenReturn(testLabel2);
        when(mockedBeastController3.render()).thenReturn(testLabel3);

        doNothing().when(onCloseRequest).run();
        when(trainerService.getTrainerMonsters(any(), any())).thenReturn(Observable.just(monsters));
        when(prefs.getRegionID()).thenReturn("");
        when(cache.getTrainer()).thenReturn(new Trainer(null, null, "TRAINER_ID", null, null, null, null, null, 0, "", 0, 0, 0, new NPCInfo(false, false, false, false, List.of(), List.of())));
        app.start(stage);
        app.show(beastListController);
        stage.requestFocus();
    }

    @Test
    public void renderTest() {
        VBox beasts = lookup("#VBoxBeasts").query();
        assertEquals(3, beasts.getChildren().size());
    }

    @Test
    public void closeTest() {
        clickOn("#CloseButtonTestId");
        verify(onCloseRequest, times(1)).run();
    }

    @Test
    public void handleKeyTest() {
        press(KeyCode.B);
        verify(onCloseRequest, times(1)).run();
    }

    @Test
    public void destroyTest() {
        beastListController.destroy();
        verify(mockedBeastController1, times(1)).destroy();
        verify(mockedBeastController2, times(1)).destroy();
        verify(mockedBeastController3, times(1)).destroy();
    }
}