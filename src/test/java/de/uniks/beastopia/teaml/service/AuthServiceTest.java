package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.controller.LoginController;
import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Spy
    TokenStorage tokenStorage;
    @Mock
    AuthApiService authApiService;
    @Mock
    UserApiService userApiService;

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
        final LoginResult loginResult = authService.login("string", "stringst").blockingFirst();

        // check values:
        assertEquals("string", loginResult.name());
        assertEquals("id", loginResult._id());
        assertEquals(STATUS_ONLINE, loginResult.status());
        assertEquals("avatar", loginResult.avatar());
        assertEquals("123", loginResult.accessToken());
        assertEquals("abc", loginResult.refreshToken());

        // check mocks
        verify(authApiService).login(new LoginDto("string", "stringst"));
    }

    @Test
    void refresh() {
        //No usage yet
    }

    @Test
    void logout() {
        //No usage yet
    }
}