package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.*;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import de.uniks.beastopia.teaml.utils.PlayerState;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngameControllerTest extends ApplicationTest {

    @Mock
    Provider<PauseController> pauseControllerProvider;
    @Mock
    Provider<EntityController> entityControllerProvider;
    @Mock
    EntityController playerController;
    @Mock
    AreaService areaService;
    @Mock
    PresetsService presetsService;
    @Mock
    UDPEventListener udpEventListener;
    @Mock
    DataCache cache;
    @Mock
    Prefs prefs;
    @Mock
    ScoreboardController scoreboardController = mock();
    @Mock
    ObjectProperty<PlayerState> state;
    @Mock
    TrainerService trainerService;
    @Mock
    TokenStorage tokenStorage;
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    IngameController ingameController;
    TileSetDescription tileSetDescription = new TileSetDescription(0, "SOURCE");
    TileSet tileSet = new TileSet(2, "IMAGE", 2, 2, 0, "NAME", 0, 4, 1);
    Chunk chunk = new Chunk(List.of(0, 1, 2, 3), 2, 2, 0, 0);
    Layer layer = new Layer(List.of(chunk), null, 1, 0, 0, null, true, 2, 2, 0, 0);
    Map map = new Map(List.of(tileSetDescription), List.of(layer), 2, 24, 4);
    Area area = new Area(null, null, "ID_AREA", "ID_REGION", "AREA_NAME", map);
    Spawn spawn = new Spawn("ID_AREA", 0, 0);
    Region region = new Region(null, null, "ID", "NAME", spawn);
    Image image = createImage(2, 2, List.of(new Color(255, 0, 255), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0)));
    Trainer trainer = new Trainer(null, null, "ID_TRAINER", "ID_REGION", "ID_USER", "TRAINER_NAME", "TRAINER_IMAGE", 0, "ID_AREA", 0, 0, 0, new NPCInfo(false));
    User user = new User(null, null, "ID_USER", "USER_NAME", "USER_STATUS", "USER_AVATAR", List.of());

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(tokenStorage.getCurrentUser()).thenReturn(user);
        when(trainerService.getAllTrainer(any())).thenReturn(Observable.just(List.of(trainer)));
        doNothing().when(scoreboardController).init();
        when(scoreboardController.render()).thenReturn(new Pane());
        doNothing().when(prefs).setCurrentRegion(any());
        doNothing().when(prefs).setArea(any());
        when(areaService.getAreas(anyString())).thenReturn(Observable.just(List.of(area)));
        doNothing().when(cache).setAreas(any());
        when(presetsService.getTileset(tileSetDescription)).thenReturn(Observable.just(tileSet));
        when(presetsService.getImage(tileSet)).thenReturn(Observable.just(image));
        when(entityControllerProvider.get()).thenReturn(playerController);
        doNothing().when(playerController).setTrainer(any());
        when(playerController.playerState()).thenReturn(new SimpleObjectProperty<>(PlayerState.IDLE));
        doNothing().when(playerController).setOnTrainerUpdate(any());
        doNothing().when(playerController).init();
        when(playerController.render()).thenReturn(new Pane());
        ingameController.setRegion(region);

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

    @Test
    void movePlayer() {
        when(cache.getTrainer()).thenReturn(trainer);
        doNothing().when(udpEventListener).send(anyString());
        type(KeyCode.W);
        type(KeyCode.S);
        type(KeyCode.A);
        type(KeyCode.D);
        verify(state, times(4)).setValue(PlayerState.WALKING);
    }

    @SuppressWarnings("SameParameterValue")
    private static Image createImage(int width, int height, List<Color> colors) {
        // create buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int i = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            if (i >= colors.size()) {
                break;
            }
            for (int x = 0; x < image.getWidth(); x++) {
                if (i >= colors.size()) {
                    break;
                }
                image.setRGB(x, y, colors.get(i++).getRGB());
            }
        }
        return convertToFxImage(image);
    }

    // sauce: https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
}