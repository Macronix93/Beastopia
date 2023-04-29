package de.uniks.beastopia.teaml;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        new App().start(stage);
    }

    @Test
    void testLoading() {
        final Label loading = lookup("Loading...").query();
        assertNotNull(loading);
    }
}