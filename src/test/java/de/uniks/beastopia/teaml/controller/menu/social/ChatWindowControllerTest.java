package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatWindowControllerTest extends ApplicationTest {

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    @Mock
    MessageService messageService;

    @InjectMocks
    ChatWindowController chatWindowController;

    @Mock
    Provider<MessageBubbleController> messageBubbleControllerProvider;

    @Mock
    MessageBubbleController messageBubbleController;

    @Spy
    App app;

    List<Message> messages = List.of(
            new Message(null, null, "0", null, "hey"),
            new Message(null, null, "1", null, "du")
    );

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        Group group = new Group(null, null, "id", "name", null);
        chatWindowController.setupChatWindowController("group", group);

        Mockito.when(messageService.getMessagesFromGroup(any())).thenReturn(Observable.just(messages));
        when(messageBubbleControllerProvider.get()).thenReturn(messageBubbleController);

        app.start(stage);
        app.show(chatWindowController);
        stage.requestFocus();
    }

    @Test
    void fillInMessages() {
        chatWindowController.fillInMessages(messages);

        for (Message message : messages) {
            boolean messageFound = false;
            for (Node node : chatWindowController.msgList.getChildren()) {
                if (node instanceof Parent parent) {
                    String text = parent.toString();

                    if (text.contains(message.body())) {
                        messageFound = true;
                        break;
                    }
                }
            }
            assertTrue(messageFound);
        }
    }
}
