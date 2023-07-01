package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class DialogWindowControllerTest extends ApplicationTest {

    @Spy
    App app;
    @Spy
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @Override
    public void start(Stage stage) {

    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleDialogWindow"));
    }

    @Test
    public void setOnCloseRequestedTest() {

    }

    @Test
    public void setOnButtonClickedTest() {

    }

    @Test
    public void setChoicesTest() {

    }

    @Test
    public void setTrainerImageTest() {

    }

    @Test
    public void setButtonImagesTest() {

    }

    @Test
    public void setTextTest() {

    }

    @Test
    public void closeTest() {

    }

    @Test
    public void handleKeyEventTest() {

    }
}
