package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChatUserControllerTest extends ApplicationTest {

    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    @SuppressWarnings("unused")
    @Mock
    TokenStorage tokenStorage;
    @Mock
    Prefs prefs;
    @Mock
    DataCache cache;
    @Spy
    App app;
    @InjectMocks
    ChatUserController chatUserController;

    final User me = new User(null, null, "1", "1", null, null, null);
    final User other = new User(null, null, "2", "2", null, null, null);
    final Group testGrp = new Group(null, null, "1", "1", List.of(me._id(), other._id()));
    final Consumer<Group> onGroupClicked = mock();
    final Consumer<Group> onPinChanged = mock();

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);
        chatUserController.setGroup(testGrp);
        when(tokenStorage.getCurrentUser()).thenReturn(me);
        when(cache.getAllUsers()).thenReturn(List.of(me, other));
        when(prefs.isPinned(testGrp)).thenReturn(true);

        doNothing().when(onPinChanged).accept(any());
        doNothing().when(onGroupClicked).accept(any());
        chatUserController.setOnPinChanged(onPinChanged);
        chatUserController.setOnGroupClicked(onGroupClicked);

        app.start(stage);
        app.show(chatUserController);
        stage.requestFocus();
    }

    @Test
    public void render() {
        assertEquals(other._id(), lookup("#name").queryAs(Text.class).getText());
        sleep(5000);
    }

    @Test
    public void setOnGroupClicked() {
        clickOn("#name");
        verify(onGroupClicked).accept(testGrp);
    }

    @Test
    public void setOnPinChanged() {
        doNothing().when(prefs).setPinned(testGrp, false);
        clickOn("#pinGroupBtn");
        verify(prefs).setPinned(testGrp, false);
        verify(onPinChanged).accept(testGrp);
    }

}