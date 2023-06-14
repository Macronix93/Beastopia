package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
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
import java.util.List;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatGroupControllerTest extends ApplicationTest {
    @InjectMocks
    ChatGroupController chatGroupController;

    @Mock
    GroupListService groupListService;
    @Mock
    Provider<DirectMessageController> directMessageControllerProvider;
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Prefs prefs;

    @Spy
    App app = new App(null);
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    Group group;

    @Override
    public void start(Stage stage) throws Exception {
        AppPreparer.prepare(app);

        group = new Group(null, null, "id", "test", null);
        chatGroupController.setGroup(group);

        app.start(stage);
        app.show(chatGroupController);
        stage.requestFocus();
    }

    @Test
    void deleteGroupOneMember() {
        group = new Group(null, null, "id2", "group", List.of("a"));
        chatGroupController.setGroup(group);

        when(groupListService.deleteGroup(group)).thenReturn(Observable.just(group));
        DirectMessageController mocked = mock();
        when(mocked.render()).thenReturn(new Label());
        when(directMessageControllerProvider.get()).thenReturn(mocked);

        clickOn("#deleteGroupBtn");

        verify(app).show(mocked);
    }

    @Test
    void deleteGroupMoreMembers() {
        group = new Group(null, null, "id2", "group", List.of("a", "b"));
        chatGroupController.setGroup(group);

        when(groupListService.removeMember(group, "a")).thenReturn(Observable.just(group));
        DirectMessageController mocked = mock();
        when(mocked.render()).thenReturn(new Label());
        when(directMessageControllerProvider.get()).thenReturn(mocked);
        when(tokenStorage.getCurrentUser()).thenReturn(new User(null, null, "a", "string", null, null, null));

        clickOn("#deleteGroupBtn");

        verify(app).show(mocked);
        sleep(5000);
    }

    @Test
    void pinGroupTest() {
        when(prefs.isPinned(any(Group.class))).thenReturn(false, true);
        doNothing().when(prefs).setPinned(any(Group.class), anyBoolean());
        clickOn("#pinGroupBtn");
        verify(prefs, times(1)).setPinned(any(Group.class), eq(true));
        clickOn("#pinGroupBtn");
        verify(prefs, times(1)).setPinned(any(Group.class), eq(false));
    }
}
