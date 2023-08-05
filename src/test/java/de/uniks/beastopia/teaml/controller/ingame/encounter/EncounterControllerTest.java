package de.uniks.beastopia.teaml.controller.ingame.encounter;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.AbilityDto;
import de.uniks.beastopia.teaml.rest.AbilityMove;
import de.uniks.beastopia.teaml.rest.ChangeMonsterMove;
import de.uniks.beastopia.teaml.rest.Encounter;
import de.uniks.beastopia.teaml.rest.Monster;
import de.uniks.beastopia.teaml.rest.MonsterAttributes;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Opponent;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.UseItemMove;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.EncounterOpponentsService;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Variant;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncounterControllerTest extends ApplicationTest {
    @Mock
    Provider<RenderBeastController> renderBeastControllerProvider;
    @Mock
    Provider<BeastInfoController> beastInfoControllerProvider;
    @Mock
    Provider<EnemyBeastInfoController> enemyBeastInfoControllerProvider;
    @Mock
    Provider<ChangeBeastController> changeBeastControllerProvider;
    @Mock
    PresetsService presetsService;
    @Mock
    EncounterOpponentsService encounterOpponentsService;
    @Mock
    DataCache cache;
    @Mock
    EventListener eventListener;
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    EncounterController encounterController;

    final MonsterAttributes attributes = new MonsterAttributes(1, 1, 1, 1);
    final MonsterAttributes currentAttributes = new MonsterAttributes(1, 0, 0, 0);
    final Monster monster1 = new Monster(null, null, "MONSTER_1", "TRAINER_ID", 2, 1, 1, Map.of("1", 1, "2", 2, "3", 1, "4", 2), attributes, currentAttributes);
    final Monster monster2 = new Monster(null, null, "MONSTER_2", "TRAINER_ID", 4, 2, 5, Map.of("1", 1, "2", 2, "3", 1, "4", 2), attributes, currentAttributes);
    final Encounter encounter = new Encounter(null, null, "ID", "r", true);
    final Opponent opponent = new Opponent(null, null, "ido", "e",
            "t", true, true, "m", null, null, 0);
    final Trainer trainer1 = new Trainer(null, null, "ID_TRAINER", "ID_REGION", "ID_USER", "TRAINER_NAME", "TRAINER_IMAGE", null, List.of(), List.of(), 0, "ID_AREA", 0, 0, 0, new NPCInfo(false, false, false, false, List.of(), List.of(), List.of()));
    final Trainer trainer2 = new Trainer(null, null, "ID_TRAINER2", "ID_REGION", "ID_USER2", "TRAINER_NAME", "TRAINER_IMAGE", null, List.of(), List.of(), 0, "ID_AREA", 0, 0, 0, new NPCInfo(false, false, false, false, List.of(), List.of(), List.of()));
    final RenderBeastController renderBeastController1 = mock();
    final RenderBeastController renderBeastController2 = mock();
    final BeastInfoController beastInfoController = mock();
    final EnemyBeastInfoController enemyBeastInfoController = mock();
    final Region region = new Region(null, null, "id", "Alb", null, null);

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        Label testLabel1 = new javafx.scene.control.Label("Label1");
        testLabel1.setId("testLabelID1");
        Label testLabel2 = new javafx.scene.control.Label("Label2");
        testLabel2.setId("testLabelID2");

        when(cache.getTrainer()).thenReturn(trainer1);
        when(cache.getCurrentEncounter()).thenReturn(encounter);
        when(cache.getCurrentOpponents()).thenReturn(List.of(opponent));
        when(cache.getJoinedRegion()).thenReturn(region);
        when(presetsService.getAbility(anyInt())).thenReturn(Observable.just(new AbilityDto(0, "ABILITY", "DESC", "TYPE", 10, 1, 1)));
        when(eventListener.listen(any(), any())).thenReturn(Observable.empty());
        when(encounterOpponentsService.getEncounterOpponents(any(), any())).thenReturn(Observable.just(List.of(opponent)));

        AtomicInteger callGet = new AtomicInteger(0);
        when(renderBeastControllerProvider.get()).thenAnswer(invocation -> {
            if (callGet.get() == 0) {
                callGet.set(1);
                return renderBeastController1;
            } else if (callGet.get() == 1) {
                callGet.set(2);
                return renderBeastController2;
            }
            return null;
        });
        when(renderBeastController1.setMonster1(any())).thenReturn(renderBeastController1);
        when(renderBeastController2.setMonster1(any())).thenReturn(renderBeastController2);
        when(renderBeastController1.render()).thenReturn(testLabel1);
        when(renderBeastController2.render()).thenReturn(testLabel2);

        when(beastInfoControllerProvider.get()).thenReturn(beastInfoController);
        when(enemyBeastInfoControllerProvider.get()).thenReturn(enemyBeastInfoController);

        when(beastInfoController.setMonster(any())).thenReturn(beastInfoController);
        when(beastInfoController.getMonster()).thenReturn(monster1);
        when(enemyBeastInfoController.setMonster(any())).thenReturn(enemyBeastInfoController);
        when(enemyBeastInfoController.getMonster()).thenReturn(monster2);

        when(beastInfoController.render()).thenReturn(testLabel1);
        when(enemyBeastInfoController.render()).thenReturn(testLabel2);


        encounterController.setOwnMonster(monster1);
        encounterController.setAllyMonster(monster1);
        encounterController.setEnemyMonster(monster2);
        encounterController.setEnemyAllyMonster(monster2);
        encounterController.setMyTrainer(trainer1);
        encounterController.setAllyTrainer(trainer1);
        encounterController.setEnemyTrainer(trainer2);
        encounterController.setEnemyAllyTrainer(trainer2);
        encounterController.setOwnMonsters(List.of(monster1));
        encounterController.setEnemyMonsters(List.of(monster2));
        encounterController.setToUpdateUIOnChange();

        app.start(stage);
        app.show(encounterController);
        stage.requestFocus();
    }

    @Test
    void getTitle() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleEncounter"));
    }

    @Test
    void wildEncounter() {
        final Encounter wild = new Encounter(null, null, "ID", "r", true);
        cache.setCurrentEncounter(wild);
    }

    @Test
    void useAttack1() {
        AbilityMove abilityMove = new AbilityMove("ability", 0, "000000000000000000000000");
        Variant<AbilityMove, ChangeMonsterMove, UseItemMove> move = new Variant<>();
        move.setT(abilityMove);

        when(encounterOpponentsService.updateEncounterOpponent("id", "ID", null, null, move.getT()))
                .thenReturn(Observable.just(opponent));

        clickOn("#attackBox1");
    }

    @Test
    void useAttack2() {
        AbilityMove abilityMove = new AbilityMove("ability", 0, "000000000000000000000000");
        Variant<AbilityMove, ChangeMonsterMove, UseItemMove> move = new Variant<>();
        move.setT(abilityMove);

        when(encounterOpponentsService.updateEncounterOpponent("id", "ID", null, null, move.getT()))
                .thenReturn(Observable.just(opponent));

        clickOn("#attackBox2");
    }

    @Test
    void useAttack3() {
        AbilityMove abilityMove = new AbilityMove("ability", 0, "000000000000000000000000");
        Variant<AbilityMove, ChangeMonsterMove, UseItemMove> move = new Variant<>();
        move.setT(abilityMove);

        when(encounterOpponentsService.updateEncounterOpponent("id", "ID", null, null, move.getT()))
                .thenReturn(Observable.just(opponent));

        clickOn("#attackBox3");
    }

    @Test
    void useAttack4() {
        AbilityMove abilityMove = new AbilityMove("ability", 0, "000000000000000000000000");
        Variant<AbilityMove, ChangeMonsterMove, UseItemMove> move = new Variant<>();
        move.setT(abilityMove);

        when(encounterOpponentsService.updateEncounterOpponent("id", "ID", null, null, move.getT()))
                .thenReturn(Observable.just(opponent));

        clickOn("#attackBox4");
    }

    @Test
    void showChangeBeasts() {
        ChangeBeastController mocked = mock();
        when(changeBeastControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Button());

        clickOn("#changeMonster");

        verify(changeBeastControllerProvider).get();
        verify(mocked).render();
    }
}
