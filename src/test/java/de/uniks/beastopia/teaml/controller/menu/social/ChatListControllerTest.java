package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatListControllerTest extends ApplicationTest {
    @Mock
    GroupListService groupListService;
    @SuppressWarnings("unused")
    @Mock
    Provider<ChatUserController> chatUserControllerProvider;
    @Mock
    Provider<ChatGroupController> chatGroupControllerProvider;
    @SuppressWarnings("unused")
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Prefs prefs;
    @Mock
    EventListener eventListener;

    @Spy
    App app;
    @SuppressWarnings("unused")
    @Spy
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang", Locale.forLanguageTag("en"));

    @InjectMocks
    ChatListController chatListController;

    final List<Group> groups = new ArrayList<>(List.of(
            new Group(null, null, "1", "1", List.of()),
            new Group(null, null, "2", "2", List.of())));

    ChatGroupController mockedChatGroupController1;
    ChatGroupController mockedChatGroupController2;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        when(eventListener.listen(any(), any())).thenReturn(Observable.empty());
        when(groupListService.getGroups()).thenReturn(Observable.just(groups));
        when(prefs.isPinned(groups.get(0))).thenReturn(true);
        when(prefs.isPinned(groups.get(1))).thenReturn(false);
        mockedChatGroupController1 = mock();
        mockedChatGroupController2 = mock();

        doNothing().when(mockedChatGroupController1).setOnGroupClicked(any());
        doNothing().when(mockedChatGroupController2).setOnGroupClicked(any());
        when(mockedChatGroupController1.setGroup(any())).thenReturn(mockedChatGroupController1);
        when(mockedChatGroupController2.setGroup(any())).thenReturn(mockedChatGroupController2);
        doNothing().when(mockedChatGroupController1).init();
        doNothing().when(mockedChatGroupController2).init();
        doNothing().when(mockedChatGroupController1).setOnPinChanged(any());
        doNothing().when(mockedChatGroupController2).setOnPinChanged(any());
        when(mockedChatGroupController1.render()).thenReturn(new Label(groups.get(0).name()));
        when(mockedChatGroupController2.render()).thenReturn(new Label(groups.get(1).name()));

        AtomicInteger call = new AtomicInteger(0);
        when(chatGroupControllerProvider.get()).thenAnswer(invocation -> {
            if (call.getAndIncrement() % 2 == 0) {
                return mockedChatGroupController1;
            } else {
                return mockedChatGroupController2;
            }
        });

        app.start(stage);
        app.show(chatListController);
        stage.requestFocus();
    }

    @Test
    public void render() {
        verify(eventListener, times(1)).listen("groups.*.*", Group.class);
        verify(groupListService, times(1)).getGroups();
        verify(prefs, atLeastOnce()).isPinned(groups.get(0));
        verify(prefs, atLeastOnce()).isPinned(groups.get(1));
        verify(mockedChatGroupController1, times(1)).setGroup(groups.get(0));
        verify(mockedChatGroupController2, times(1)).setGroup(groups.get(1));
        verify(mockedChatGroupController1, times(1)).init();
        verify(mockedChatGroupController2, times(1)).init();
        verify(mockedChatGroupController1, times(1)).render();
        verify(mockedChatGroupController2, times(1)).render();
    }


}