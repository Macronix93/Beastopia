package de.uniks.beastopia.teaml;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new App().start(stage);
    }

    @Test
    void testLoading() {
        assertEquals("Beastopia - Registration", stage.getTitle());
    }
}