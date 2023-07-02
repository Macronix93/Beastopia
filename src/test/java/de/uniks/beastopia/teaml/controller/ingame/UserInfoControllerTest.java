package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInfoControllerTest extends ApplicationTest {
    @Spy
    final ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));
    @Mock
    DataCache cache;
    @InjectMocks
    UserInfoController userInfoController;
    private final App app = new App();
    private final List<Achievement> userAchievements = List.of(new Achievement(null, null, "ID", "Leon", null, 25));
    private final Map<String, String> achievementDescription = new HashMap<>();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(cache.getAchievementDescriptions()).thenReturn(achievementDescription);

        userInfoController
                .setAchievements(3)
                .setTotalAchievements(6)
                .setName("Leon")
                .setUserAchievements(userAchievements);

        app.start(stage);
        app.show(userInfoController);
        stage.requestFocus();
    }

    @Test
    void render() {
        var node = lookup("3/6 " + resources.getString("achievements")).query();
        assertNotNull(node);
    }
}