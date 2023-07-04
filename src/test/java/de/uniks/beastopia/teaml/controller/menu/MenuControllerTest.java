package de.uniks.beastopia.teaml.controller.menu;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.AppPreparer;
import de.uniks.beastopia.teaml.controller.auth.LoginController;
import de.uniks.beastopia.teaml.controller.menu.social.FriendListController;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AuthService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MenuControllerTest extends ApplicationTest {

    @Mock
    Provider<RegionController> regionControllerProvider;
    @Mock
    Provider<FriendListController> friendListControllerProvider;
    @Mock
    Provider<LoginController> loginControllerProvider;
    @Mock
    Provider<EditProfileController> editProfileControllerProvider;
    @Mock
    Provider<SettingsController> settingsControllerProvider;
    @Mock
    AuthService authService;
    @Mock
    TokenStorage tokenStorage;
    @SuppressWarnings("unused")
    @Mock
    DataCache cache;
    @Mock
    Prefs prefs;
    @Spy
    @SuppressWarnings("unused")
    App app;
    @InjectMocks
    MenuController menuController;
    @Spy
    @SuppressWarnings("unused")
    final
    ResourceBundle resources = ResourceBundle.getBundle("de/uniks/beastopia/teaml/assets/lang");
    final Achievement achievement = new Achievement(null, null, "ID", "ID_USER", null, 100);
    final String visitedArea = "AREA";

    RegionController mockedRegionController;
    FriendListController mockedFriendListController;

    @Override
    public void start(Stage stage) {
        AppPreparer.prepare(app);

        mockedRegionController = mock(RegionController.class);
        mockedFriendListController = mock(FriendListController.class);

        when(regionControllerProvider.get()).thenReturn(mockedRegionController);
        when(friendListControllerProvider.get()).thenReturn(mockedFriendListController);

        when(mockedRegionController.render()).thenReturn(new Label("RegionControllerLabelTest"));
        when(mockedFriendListController.render()).thenReturn(new Label("FriendListControllerLabelTest"));

        when(cache.getMyAchievements()).thenReturn(List.of(achievement));
        when(prefs.getVisitedAreas()).thenReturn(visitedArea);

        User mockedUser = mock(User.class);
        when(tokenStorage.getCurrentUser()).thenReturn(mockedUser);

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
        Text userName = lookup("#userName").query();
        assertNotNull(userName);
        Button editProfileBtn = lookup("#editProfileBtn").query();
        assertNotNull(editProfileBtn);
    }

    @Test
    public void logout() {
        User mocked = mock(User.class);
        when(authService.logout()).thenReturn(Observable.just(mocked));

        final LoginController mock = Mockito.mock(LoginController.class);
        when(loginControllerProvider.get()).thenReturn(mock);
        doNothing().when(app).show(mock);

        clickOn("#logoutBtn");

        verify(app).show(mock);
    }

    @Test
    void title() {
        assertEquals(app.getStage().getTitle(), resources.getString("titleMenu"));
    }

    @Test
    public void editProfileButtonPressed() {
        EditProfileController mocked = mock(EditProfileController.class);
        when(editProfileControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());
        clickOn("#editProfileBtn");
        verify(app).show(mocked);
    }

    @Test
    public void settingsButtonPressed() {
        SettingsController mocked = mock(SettingsController.class);
        when(settingsControllerProvider.get()).thenReturn(mocked);
        when(mocked.render()).thenReturn(new Label());
        clickOn("#settingsBtn");
        verify(app).show(mocked);
    }
}
