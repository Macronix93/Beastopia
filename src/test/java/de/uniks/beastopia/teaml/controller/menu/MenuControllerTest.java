package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
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
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MenuControllerTest extends ApplicationTest {

    @Mock
    Provider<RegionController> regionControllerProvider;
    @Mock
    Provider<FriendListController> friendListControllerProvider;
    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    AuthService authService;
    @Spy
    @SuppressWarnings("unused")
    App app;
    @InjectMocks
    MenuController menuController;
    @Spy
    @SuppressWarnings("unused")
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");

    RegionController mockedRegionController;
    FriendListController mockedFriendListController;

    @Override
    public void start(Stage stage) throws Exception {
        mockedRegionController = mock(RegionController.class);
        mockedFriendListController = mock(FriendListController.class);

        when(regionControllerProvider.get()).thenReturn(mockedRegionController);
        when(friendListControllerProvider.get()).thenReturn(mockedFriendListController);

        when(mockedRegionController.render()).thenReturn(new Label("RegionControllerLabelTest"));
        when(mockedFriendListController.render()).thenReturn(new Label("FriendListControllerLabelTest"));

        app.start(stage);
        app.show(menuController);
        stage.requestFocus();

        verify(regionControllerProvider, times(1)).get();
        verify(friendListControllerProvider, times(1)).get();
        verify(mockedRegionController, times(1)).render();
        verify(mockedFriendListController, times(1)).render();
    }

    @Test
    public void testRender() {
        Label regionControllerLabel = lookup("RegionControllerLabelTest").query();
        assertNotNull(regionControllerLabel);
        Label friendListControllerLabel = lookup("FriendListControllerLabelTest").query();
        assertNotNull(friendListControllerLabel);
    }

    @Test
    public void logout() {
        User mocked = mock(User.class);
        when(authService.logout()).thenReturn(Observable.just(mocked));

        final LoginController mock = Mockito.mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        clickOn("#logoutButton");

        verify(app).show(mock);
    }
}