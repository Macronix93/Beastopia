package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.function.Consumer;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DialogWindowControllerTest extends ApplicationTest {

    @Spy
    App app;
    @InjectMocks
    DialogWindowController dialogWindowController;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        Image trainerImage = new Image("de/uniks/beastopia/teaml/assets/user.png");
        List<Image> buttonImages = List.of(new Image("de/uniks/beastopia/teaml/assets/user.png"), new Image("de/uniks/beastopia/teaml/assets/user.png"));

        dialogWindowController.setChoices(List.of("Button 1", "Button 2"));
        dialogWindowController.setText("Welcome! Please select a Beast.");
        dialogWindowController.setTrainerImage(trainerImage);
        dialogWindowController.setButtonImages(buttonImages);

        app.start(stage);
        app.show(dialogWindowController);
        stage.requestFocus();
    }

    @Test
    public void closeTest() {
        Runnable onClose = Mockito.mock();
        dialogWindowController.setOnCloseRequested(onClose);

        clickOn("#closeButton");

        verify(onClose).run();
    }

    @Test
    public void handleKeyEventTest() {
        Runnable onClose = Mockito.mock();
        dialogWindowController.setOnCloseRequested(onClose);

        press(KeyCode.T);

        verify(onClose).run();
    }

    @Test
    public void buttonClickedTest() {
        Consumer<Integer> onButtonClicked = Mockito.mock();
        dialogWindowController.setOnButtonClicked(onButtonClicked);

        clickOn("Button 1");
        verify(onButtonClicked).accept(0);

        clickOn("Button 2");
        verify(onButtonClicked).accept(1);
    }
}
