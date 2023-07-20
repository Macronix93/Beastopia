package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.NPCInfo;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.utils.PlayerState;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityControllerTest extends ApplicationTest {

    @Mock
    PresetsService presetsService;
    @InjectMocks
    EntityController entityController;
    @Spy
    App app;

    final ObjectProperty<PlayerState> state = new SimpleObjectProperty<>(PlayerState.IDLE);
    final Image image = createImage(List.of(new Color(255, 0, 255), new Color(0, 255, 0), new Color(0, 0, 255), new Color(255, 255, 0)));
    final Trainer trainer = new Trainer(null, null, "ID_TRAINER", "ID_REGION", "ID_USER", "TRAINER_NAME", "TRAINER_IMAGE", null, List.of(), 0, "ID_AREA", 0, 0, 0, new NPCInfo(false, false, false, false, List.of(), List.of(), List.of()));

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        when(presetsService.getCharacterSprites(any(), anyBoolean())).thenReturn(Observable.just(image));
        entityController.setTrainer(trainer);
        entityController.playerState().bind(state);
        app.start(stage);
        app.show(entityController);
        stage.requestFocus();
    }

    @Test
    void render() {
        assertNotNull(lookup("#entityView"));
    }


    private static Image createImage(List<Color> colors) {
        // create buffered image
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
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