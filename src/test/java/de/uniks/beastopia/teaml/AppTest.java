package de.uniks.beastopia.teaml;

import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class AppTest extends ApplicationTest {
    private Stage stage;
    private final App app = new App(null);
    private final TestComponent component = (TestComponent) DaggerTestComponent.builder().mainApp(app).build();

    @Override
    public void start(Stage stage) {
    }

}