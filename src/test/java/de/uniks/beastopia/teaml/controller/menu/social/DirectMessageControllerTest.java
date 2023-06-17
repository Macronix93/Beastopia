package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Message;
import de.uniks.beastopia.teaml.service.MessageService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DirectMessageControllerTest extends ApplicationTest {
    @Spy
    final
    App app = new App(null);
    @Mock
    Provider<ChatWindowController> chatWindowControllerProvider;
    @Mock
    ChatListController chatListController;
    @Mock
    @SuppressWarnings("unused")
    EventListener eventListener;
    @Mock
    MessageService messageService;
    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @InjectMocks
    DirectMessageController directMessageController;
    Group group;
    ChatWindowController mocked;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Consumer<Group>> captor = ArgumentCaptor.forClass(Consumer.class);
        doNothing().when(chatListController).setOnGroupClicked(captor.capture());
        when(chatListController.render()).thenReturn(new Label("chatList"));

        group = new Group(null, null, "GROUP", "group", List.of());
        mocked = Mockito.mock();
        when(mocked.setupChatWindowController(any())).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label("chatWindow"));
        when(chatWindowControllerProvider.get()).thenReturn(mocked);

        app.start(stage);
        app.show(directMessageController);
        stage.requestFocus();

        Consumer<Group> onGroupClicked = captor.getValue();
        onGroupClicked.accept(group);
    }

    @Test
    void back() {
        final MenuController mock = Mockito.mock(MenuController.class);
        when(mock.render()).thenReturn(new Label("IngameController"));
        doNothing().when(mock).init();

        app.setHistory(List.of(mock));

        clickOn("#backButton");

        verify(mock).render();
    }

    @Test
    void sendMessage() {
        when(messageService.sendMessageToGroup(eq(group), any()))
                .thenReturn(Observable.just(new Message(
                        null, null, "MESSAGE",
                        "Sender", "Hello World")));

        clickOn("#chatInput");
        write("Hello World");
        clickOn("#sendButton");

        verify(messageService).sendMessageToGroup(eq(group), any());
        verify(chatWindowControllerProvider).get();
        verify(mocked).render();
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleDirectMessage"));
    }

}