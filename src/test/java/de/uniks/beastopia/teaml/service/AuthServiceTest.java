package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    Prefs prefs;

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
        when(prefs.isRememberMe()).thenReturn(true);
        when(prefs.getRememberMeToken()).thenReturn("abc");
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
        doNothing().when(prefs).clearRememberMe();
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
        verify(prefs).clearRememberMe();
        verify(tokenStorage, times(2)).getCurrentUser();
        verify(userApiService).updateUser("c", dto);
        verify(authApiService).logout();
    }

    @Test
    void updatePassword() {
        // define mocks:
        String newPassword = "newPassword";
        User mockedUser = mock(User.class);
        when(tokenStorage.getCurrentUser()).thenReturn(mockedUser);
        when(mockedUser._id()).thenReturn("Testid");
        when(userApiService.updateUser(anyString(), any(UpdateUserDto.class))).thenReturn(Observable.just(mockedUser));

        // action:
        Observable<User> updateUserObservable = authService.updatePassword(newPassword);

        //check mocks
        assertEquals(mockedUser, updateUserObservable.blockingFirst());
        verify(tokenStorage).getCurrentUser();
        verify(userApiService).updateUser("Testid", new UpdateUserDto(null, null, null, null, newPassword));
    }

    @Test
    void deleteUser() {
        // define mocks:
        User currentUser = new User(null, null, "Testid", null, null, null, null);
        when(tokenStorage.getCurrentUser()).thenReturn(currentUser);
        when(userApiService.deleteUser("Testid")).thenReturn(Observable.empty());

        // action:
        Observable<User> deleteUserObservable = authService.deleteUser();

        //check mocks
        assertEquals(currentUser, deleteUserObservable.blockingFirst());
        verify(tokenStorage, times(2)).getCurrentUser();
        verify(userApiService).deleteUser("Testid");
    }
}