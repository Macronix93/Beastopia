package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class RegionInfoControllerTest extends ApplicationTest {

    @Spy
    App app;
    @InjectMocks
    RegionInfoController regionInfoController;

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);
        regionInfoController.setText("Text", "Text");
        app.start(stage);
        app.show(regionInfoController);
        stage.requestFocus();
    }

    @Test
    void changeTextTest() {
        assertEquals("Text", lookup("#place").queryAs(Text.class).getText());
        assertEquals("Text", lookup("#description").queryAs(Text.class).getText());

        regionInfoController.setText("ChangedText", "ChangedText");
        app.show(regionInfoController);

        assertEquals("ChangedText", lookup("#place").queryAs(Text.class).getText());
        assertEquals("ChangedText", lookup("#description").queryAs(Text.class).getText());
    }

    @Test
    void emptyDescriptionTest() {
        regionInfoController.setText("Text", "");
        app.show(regionInfoController);

        assertEquals("Text", lookup("#place").queryAs(Text.class).getText());
        assertFalse(lookup("#description").query().visibleProperty().get());
    }

}