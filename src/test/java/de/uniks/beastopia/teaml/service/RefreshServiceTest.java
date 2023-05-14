package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.AuthApiService;
import de.uniks.beastopia.teaml.rest.LoginResult;
import de.uniks.beastopia.teaml.rest.RefreshDto;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshServiceTest {

    @Mock
    AuthApiService authApiService;
    @InjectMocks
    RefreshService refreshService;

    @Test
    void refresh() {
        when(authApiService.refresh(new RefreshDto("TOKEN")))
                .thenReturn(Observable.just(new LoginResult(null, null, "ID", "NAME", null,
                        null, null, "NEW A-TOKEN", "NEW R-TOKEN")));
        LoginResult result = refreshService.refresh("TOKEN").blockingFirst();
        assertEquals("NEW A-TOKEN", result.accessToken());
        assertEquals("NEW R-TOKEN", result.refreshToken());
        verify(authApiService).refresh(new RefreshDto("TOKEN"));
    }
}