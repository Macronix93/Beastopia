package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresetsServiceTest {

    @Mock
    PresetsApiService presetsApiService;

    @InjectMocks
    PresetsService presetsService;

    final List<String> allSprites = List.of("A.png", "B.png");

    final ResponseBody responseBody = ResponseBody.create(MediaType.parse("text/html"), "<h1>Fake</h1>");

    @Test
    void getCharacters() {
        when(presetsApiService.getCharacters()).thenReturn(Observable.just(allSprites));

        List<String> responseList = presetsService.getCharacters().blockingFirst();

        assertEquals("A.png", responseList.get(0));
        assertEquals("B.png", responseList.get(1));
        verify(presetsApiService).getCharacters();
    }

    @Test
    void getSpriteSheet() {
        when(presetsApiService.getCharacterSprites("A.png")).thenReturn(Observable.just(responseBody));

        Image response = presetsService.getCharacterSprites("A.png").blockingFirst();

        assertNull(response.getUrl());
        verify(presetsApiService).getCharacterSprites("A.png");
    }
}