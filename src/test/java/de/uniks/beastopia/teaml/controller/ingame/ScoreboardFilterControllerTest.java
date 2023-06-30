package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoreboardFilterControllerTest extends ApplicationTest {
    @Mock
    DataCache cache;
    @InjectMocks
    ScoreboardFilterController scoreboardFilterController;
    private final App app = new App();
    private final List<AchievementsSummary> achievementSummaries = List.of(new AchievementsSummary("123", 0, 2, 3));
    private final Map<String, String> achievementDescription = new HashMap<>();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(cache.getAchievementDescriptions()).thenReturn(achievementDescription);

        scoreboardFilterController
                .setCurrentAchievements(achievementSummaries);

        app.start(stage);
        app.show(scoreboardFilterController);
        stage.requestFocus();
    }

    @Test
    void removeFilter() {
    }

    @Test
    void applyFilter() {
    }
}
