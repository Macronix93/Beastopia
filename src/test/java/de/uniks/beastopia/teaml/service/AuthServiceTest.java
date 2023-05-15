package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Spy
    TokenStorage tokenStorage;
    @Mock
    AuthApiService authApiService;
    @Mock
    UserApiService userApiService;
    @Mock
    Preferences preferences;

    @InjectMocks
    AuthService authService;

    @Test
    void login() {
        // define mocks:
        doNothing().when(tokenStorage).setAccessToken("123");
        doNothing().when(tokenStorage).setRefreshToken("abc");
        doNothing().when(tokenStorage).setCurrentUser(any());
        User mocked = mock(User.class);
        when(userApiService.updateUser(anyString(), any())).thenReturn(Observable.just(mocked));
        Mockito
                .when(authApiService.login(any()))
                .thenReturn(Observable.just(
                        new LoginResult(null, null, "id", "string", STATUS_ONLINE,
                                "avatar", new ArrayList<>(), "123", "abc")));

        // action:
        final LoginResult loginResult = authService.login("string", "stringst", false).blockingFirst();

        // check values:
        assertEquals("string", loginResult.name());
        assertEquals("id", loginResult._id());
        assertEquals(STATUS_ONLINE, loginResult.status());
        assertEquals("avatar", loginResult.avatar());
        assertEquals("123", loginResult.accessToken());
        assertEquals("abc", loginResult.refreshToken());

        // check mocks
        verify(authApiService).login(new LoginDto("string", "stringst"));
        verify(tokenStorage).setAccessToken("123");
        verify(tokenStorage).setRefreshToken("abc");
        verify(tokenStorage).setCurrentUser(any(User.class));
    }

    @Test
    void refresh() {
        // define mocks:
        doNothing().when(tokenStorage).setAccessToken("123");
        doNothing().when(tokenStorage).setRefreshToken("abc");
        doNothing().when(tokenStorage).setCurrentUser(any());
        when(preferences.get("rememberMe", null)).thenReturn("abc");
        User mocked = mock(User.class);
        when(userApiService.updateUser(anyString(), any())).thenReturn(Observable.just(mocked));
        Mockito
                .when(authApiService.refresh(any()))
                .thenReturn(Observable.just(
                        new LoginResult(null, null, "id", "string", STATUS_ONLINE,
                                "avatar", new ArrayList<>(), "123", "abc")));

        // action:
        final LoginResult loginResult = authService.refresh().blockingFirst();

        // check values:
        assertEquals("string", loginResult.name());
        assertEquals("id", loginResult._id());
        assertEquals(STATUS_ONLINE, loginResult.status());
        assertEquals("avatar", loginResult.avatar());
        assertEquals("123", loginResult.accessToken());
        assertEquals("abc", loginResult.refreshToken());

        // check mocks
        verify(authApiService).refresh(new RefreshDto("abc"));
        verify(tokenStorage).setAccessToken("123");
        verify(tokenStorage).setRefreshToken("abc");
        verify(tokenStorage).setCurrentUser(any(User.class));
    }

    @Test
    void logout() {
        // define mocks:
        doNothing().when(preferences).remove("rememberMe");
        when(tokenStorage.getCurrentUser()).thenReturn(new User(null, null, "c", null, null, null, null));
        UpdateUserDto dto = new UpdateUserDto(null, UserApiService.STATUS_OFFLINE, null, null, null);
        User mockedUser = mock(User.class);
        when(userApiService.updateUser("c", dto)).thenReturn(Observable.just(mockedUser));
        when(authApiService.logout()).thenReturn(Observable.empty());

        // action:
        authService.logout().subscribe(a -> {
        }, e -> {
        }).dispose();

        //check mocks
        verify(preferences).remove("rememberMe");
        verify(tokenStorage, times(2)).getCurrentUser();
        verify(userApiService).updateUser("c", dto);
        verify(authApiService).logout();
    }
}