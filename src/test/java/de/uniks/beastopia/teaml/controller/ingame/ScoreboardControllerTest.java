package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AchievementsService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreboardControllerTest extends ApplicationTest {
    @Mock
    DataCache cache;
    @Mock
    AchievementsService achievementsService;
    @Mock
    TrainerService trainerService;
    @Mock
    Provider<ScoreboardUserItemController> scoreBoardUserItemControllerProvider;
    @Mock
    Provider<UserInfoController> userInfoControllerProvider;
    @InjectMocks
    ScoreboardController scoreboardController;
    private final App app = new App();
    private final List<AchievementsSummary> achievementSummaries = List.of(new AchievementsSummary("123", 0, 2, 3));
    private final List<Achievement> achievements = List.of(new Achievement(null, null, "ID2", "ID", new Date(), 0));
    private final List<User> users = List.of(new User(null, null, "ID", "Leon", "status", "avatar", null));
    private final List<Trainer> trainers = List.of(new Trainer(null, null, "TRAINER_ID", "region", "ID", "Lonartie", "image", 0, "area", 0, 0, 0, null));
    private final Pane pane = new Pane();
    private ArgumentCaptor<Consumer<String>> onUserClickedCaptor;

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);
        //noinspection unchecked
        onUserClickedCaptor = ArgumentCaptor.forClass(Consumer.class);

        ScoreboardUserItemController controller = mock();
        when(controller.setName(any())).thenReturn(controller);
        when(controller.setAchievements(anyInt())).thenReturn(controller);
        when(controller.setUserId(any())).thenReturn(controller);
        when(controller.setOnUserClicked(onUserClickedCaptor.capture())).thenReturn(controller);
        when(controller.setTotalAchievements(anyInt())).thenReturn(controller);
        when(controller.render()).thenReturn(pane);

        when(achievementsService.getAchievements()).thenReturn(Observable.just(achievementSummaries));
        when(trainerService.getAllTrainer(any())).thenReturn(Observable.just(trainers));
        when(cache.getAllUsers()).thenReturn(users);
        when(achievementsService.getUserAchievements(any())).thenReturn(Observable.just(achievements));
        when(scoreBoardUserItemControllerProvider.get()).thenReturn(controller);

        app.start(stage);
        app.show(scoreboardController);
        stage.requestFocus();

        sleep(2000);
    }

    @Test
    void render() {
        verify(scoreBoardUserItemControllerProvider.get()).render();
    }

    @Test
    void setOnCloseRequested() {
        Runnable runnable = mock(Runnable.class);
        doNothing().when(runnable).run();
        scoreboardController.setOnCloseRequested(runnable);

        clickOn(pane);
        type(KeyCode.N);

        verify(runnable).run();
    }

    @Test
    void onUserClicked() {
        UserInfoController mocked = mock(UserInfoController.class);
        when(mocked.render()).thenReturn(new Pane());
        when(mocked.setName(any())).thenReturn(mocked);
        when(mocked.setAchievements(anyInt())).thenReturn(mocked);
        when(mocked.setTotalAchievements(anyInt())).thenReturn(mocked);

        when(userInfoControllerProvider.get()).thenReturn(mocked);

        sleep(2000);

        Platform.runLater(() -> onUserClickedCaptor.getValue().accept("ID"));

        verify(userInfoControllerProvider.get()).render();
    }
}