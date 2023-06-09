package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.Spawn;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.service.TrainerService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest extends ApplicationTest {
    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    TrainerController trainerController;
    @Mock
    Provider<DeleteTrainerController> deleteTrainerControllerProvider;
    @Mock
    Provider<IngameController> ingameControllerProvider;
    @Mock
    DataCache cache;
    @Mock
    PresetsService presetsService;
    @Mock
    TrainerService trainerService;
    @Mock
    TokenStorage tokenStorage;

    Image image = createImage(2, 2, List.of(new Color(255, 0, 255), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0)));
    List<Pair<String, Image>> allCharacters = List.of(
            new Pair<>("A.png", image),
            new Pair<>("B.png", image)
    );
    User user = new User(null, null, "ID", "USER", "ONLINE", null, List.of());
    Region region = new Region(null, null, "ID", "NAME", new Spawn(null, 0, 0));
    List<Trainer> allTrainer = List.of(
            new Trainer(null, null, "123", "A", "123", "A", "A", 0, null, 0, 0, 0, null),
            new Trainer(null, null, "456", "B", "456", "B", "B", 1, null, 0, 0, 0, null)
    );

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        trainerController.setRegion(region);

        when(cache.getCharacters()).thenReturn(allCharacters);
        when(trainerService.getAllTrainer(region._id())).thenReturn(Observable.just(allTrainer));
        when(tokenStorage.getCurrentUser()).thenReturn(user);
        doNothing().when(mock(TrainerController.class)).showTrainerSpritePreview(any(), any());

        app.start(stage);
        app.show(trainerController);
        stage.requestFocus();
    }

    @Test
    void createTrainer() {
        IngameController mockedIngameController = mock(IngameController.class);
        when(ingameControllerProvider.get()).thenReturn(mockedIngameController);
        when(mockedIngameController.render()).thenReturn(new Button());

        when(trainerService.createTrainer(eq(region._id()), anyString(), anyString()))
                .thenReturn(Observable.just(
                        new Trainer(null, null, "ID", "REGION", "USER", "TRAINER_NAME", "TRAINER_IMAGE", 0, null, 0, 0, 0, new NPCInfo(false))));

        clickOn("#trainerNameInput");
        write("MyTrainer");
        clickOn("#saveTrainerButton");

        verify(trainerService).createTrainer(eq(region._id()), anyString(), anyString());
        verify(ingameControllerProvider).get();
        verify(mockedIngameController).render();
    }

    @Test
    void trainerDelete() {
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleTrainer"));
    }

    @Test
    void noTrainerNameEntered() {
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