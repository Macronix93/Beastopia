package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;
import retrofit2.HttpException;
import retrofit2.Response;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static java.util.Calendar.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageBubbleControllerTest extends ApplicationTest {
    @Spy
    App app;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @Mock
    EventListener eventListener;
    @Mock
    @SuppressWarnings("unused")
    FriendListService friendListService;
    @Mock
    MessageService messageService;
    @Mock
    DataCache cache;
    @InjectMocks
    MessageBubbleController messageBubbleController;

    final Date created = new GregorianCalendar(2022, JANUARY, 1).getTime();
    final Date updated = new GregorianCalendar(2022, JANUARY, 2).getTime();
    final User user = new User(created, updated, "USER", "User", "online", null, null);
    final Group group = new Group(created, updated, "GROUP", "Group", List.of(user._id()));
    final Message message = new Message(created, updated, "MESSAGE", user._id(), "Message");
    final Consumer<Pair<Parent, MessageBubbleController>> onDelete = mock();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(eventListener.listen(any(), any())).thenReturn(Observable.empty());
        when(messageService.isSentByMe(any())).thenReturn(true);
        when(cache.getAllUsers()).thenReturn(List.of(user));

        messageBubbleController.setMessage(group, message);

        app.start(stage);
        app.show(messageBubbleController);
        stage.requestFocus();
    }

    @Test
    void editMessageSuccessful() {
        Message newMessage = new Message(created, updated, "MESSAGE", user._id(), "Edited");
        when(messageService.updateMessage(group, message, "Edited")).thenReturn(Observable.just(newMessage));

        assertEquals("Message", lookup("#messageBody").queryAs(Text.class).getText());

        clickOn("#editButton");
        assertNotNull(lookup("#editMessageBody").query());
        clickOn("#editMessageBody");
        for (int i = 0; i < message.body().length(); i++) {
            type(KeyCode.BACK_SPACE);
        }
        write("Edited");
        type(KeyCode.ENTER);

        assertEquals("Edited", lookup("#messageBody").queryAs(Text.class).getText());
        verify(messageService, times(1)).updateMessage(group, message, "Edited");
    }

    @Test
    void editMessageCancelled() {
        assertEquals("Message", lookup("#messageBody").queryAs(Text.class).getText());

        clickOn("#editButton");
        assertNotNull(lookup("#editMessageBody").query());
        clickOn("#editMessageBody");
        for (int i = 0; i < message.body().length(); i++) {
            type(KeyCode.BACK_SPACE);
        }
        write("Edited");
        type(KeyCode.ESCAPE);

        assertNotNull(lookup("#messageBody").queryAs(Text.class));
        assertEquals("Message", lookup("#messageBody").queryAs(Text.class).getText());
    }

    @Test
    void deleteMessageSuccessful() {
        doNothing().when(onDelete).accept(any());
        messageBubbleController.setOnDelete(onDelete);
        when(messageService.deleteMessage(group, message)).thenReturn(Observable.just(message));

        clickOn("#deleteButton");

        verify(onDelete, times(1)).accept(any());
        verify(messageService, times(1)).deleteMessage(group, message);
    }

    @Test
    void deleteMessageFailed() {
        ResponseBody body = ResponseBody.create(MediaType.get("application/json"), "{\"message\":\"Attempt to delete messages in an inaccessible parent, or to delete someone else's message.\"}");
        when(messageService.deleteMessage(group, message)).thenReturn(Observable.error(new HttpException(Response.error(403, body))));

        clickOn("#deleteButton");

        Parent pane = lookup(".dialog-pane").query();
        Node node = from(pane).lookup((Text t) -> t.getText().contains("Attempt to delete")).query();
        assertNotNull(node);
    }
}
