package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.CreateUserDto;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.rest.UserApiService;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static de.uniks.beastopia.teaml.rest.UserApiService.STATUS_ONLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    UserApiService userApiService;

    @InjectMocks
    RegistrationService registrationService;

    @Test
    void createUser() {
        Mockito
                .when(userApiService.createUser(ArgumentMatchers.any()))
                .thenReturn(Observable.just(
                        new User(null, null, "id", "name", STATUS_ONLINE, "avatar", new ArrayList<String>())));

        final User user = registrationService.createUser("name", "avatar", "password").blockingFirst();

        assertEquals("id", user._id());
        assertEquals("name", user.name());
        assertEquals(STATUS_ONLINE, user.status());
        assertEquals("avatar", user.avatar());

        verify(userApiService).createUser(new CreateUserDto("name", "avatar", "password"));
    }
}