package de.uniks.beastopia.teaml;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest extends ApplicationTest {
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        new App().start(stage);
    }

    @Test
    void testLoading() {
        assertEquals("Beastopia - Login", stage.getTitle());
    }
}