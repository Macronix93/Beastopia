package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    MessageBubbleController messageBubbleController1;

    @Mock
    MessageBubbleController messageBubbleController2;
    @Mock
    EventListener eventListener;

    @Spy
    App app;

    private Group group;

    List<Message> messages = List.of(
            new Message(null, null, "0", null, "hey"),
            new Message(null, null, "1", null, "du")
    );

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app, eventListener);

        group = new Group(null, null, "id", "name", null);
        chatWindowController.setupChatWindowController(group);

        Mockito.when(messageService.getMessagesFromGroup(any())).thenReturn(Observable.just(messages));

        AtomicReference<Integer> call = new AtomicReference<>(0);
        when(messageBubbleControllerProvider.get()).thenAnswer(i -> {
            if (call.get() == 0) {
                call.set(1);
                return messageBubbleController1;
            } else {
                return messageBubbleController2;
            }
        });

        when(messageBubbleController1.setMessage(eq(group), any())).thenReturn(messageBubbleController1);
        when(messageBubbleController1.setOnDelete(any())).thenReturn(messageBubbleController1);
        when(messageBubbleController1.render()).thenReturn(new Label("hey"));

        when(messageBubbleController2.setMessage(eq(group), any())).thenReturn(messageBubbleController2);
        when(messageBubbleController2.setOnDelete(any())).thenReturn(messageBubbleController2);
        when(messageBubbleController2.render()).thenReturn(new Label("du"));

        app.start(stage);
        app.show(chatWindowController);
        stage.requestFocus();
    }

    @Test
    void fillInMessages() {
        for (Message message : messages) {
            assertTrue(showsMessage(message));
        }

        app.stop();

        verify(messageBubbleControllerProvider, times(2)).get();

        verify(messageBubbleController1).setMessage(group, messages.get(0));
        verify(messageBubbleController2).setMessage(group, messages.get(1));

        verify(messageBubbleController1).render();
        verify(messageBubbleController2).render();
    }

    boolean showsMessage(Message message) {
        return lookup(message.body()).query() != null;
    }
}
