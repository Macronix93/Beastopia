package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Spawn;
import de.uniks.beastopia.teaml.service.AreaService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngameControllerTest extends ApplicationTest {

    @Mock
    Provider<PauseController> pauseControllerProvider;
    @Mock
    AreaService areaService;
    @Mock
    DataCache cache;
    @Mock
    Prefs prefs;
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @InjectMocks
    IngameController ingameController;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        doNothing().when(prefs).setRegion(any());
        when(areaService.getAreas(anyString())).thenReturn(Observable.just(List.of()));
        doNothing().when(cache).setAreas(any());

        ingameController.setRegion(new Region(null, null, "ID", "NAME",
                new Spawn("AREA", 0, 0)));

        app.start(stage);
        app.show(ingameController);
        stage.requestFocus();
    }

    @Test
    void pauseMenu() {
        final PauseController mock = Mockito.mock(PauseController.class);
        when(pauseControllerProvider.get()).thenReturn(mock);
        when(mock.render()).thenReturn(new Label());

        type(KeyCode.ESCAPE);
        verify(mock).render();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleIngame"));
    }
}