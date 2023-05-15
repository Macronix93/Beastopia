package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.ResourceBundle;

@ExtendWith(MockitoExtension.class)
public class MessageBubbleControllerTest extends ApplicationTest {

    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @InjectMocks
    MessageBubbleController messageBubbleController;

    @Override
    public void start(Stage stage) throws Exception {
        app.start(stage);
        app.show(messageBubbleController);
        stage.requestFocus();
    }

    @Test
    void editMessage() {
    }

    @Test
    void deleteMessage() {
    }
}
