package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Rectangle2D;
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
import static org.mockito.ArgumentMatchers.anyInt;
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

    final AbilityDto abilityDto = new AbilityDto(0, "name", "desc", "ice", 1, 2, 3);

    final TileSet tileSet = new TileSet(1, null, 1, 1, 1, "A.png", 1, 1, 1, null);

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

        Image response = presetsService.getCharacterSprites("A.png", false).blockingFirst();

        assertNull(response.getUrl());
        verify(presetsApiService).getCharacterSprites("A.png");
    }

    @Test
    void getAbility() {
        when(presetsApiService.getAbilities()).thenReturn(Observable.just(List.of(abilityDto)));
        List<AbilityDto> result = presetsService.getAbilities().blockingFirst();
        assertEquals(abilityDto, result.get(0));
        verify(presetsApiService).getAbilities();
    }

    @Test
    void getAbilities() {
        when(presetsApiService.getAbility(anyInt())).thenReturn(Observable.just(abilityDto));
        AbilityDto result = presetsService.getAbility(1).blockingFirst();
        assertEquals(abilityDto, result);
        verify(presetsApiService).getAbility(1);
    }

    @Test
    void getItems() {
        when(presetsApiService.getItems()).thenReturn(Observable.just(List.of((new ItemTypeDto(1, "image", "name", 3, "desc", "ball")))));
        List<ItemTypeDto> result = presetsService.getItems().blockingFirst();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("image", result.get(0).image());
        assertEquals("name", result.get(0).name());
        assertEquals(3, result.get(0).price());
        assertEquals("desc", result.get(0).description());
        assertEquals("ball", result.get(0).use());
        verify(presetsApiService).getItems();
    }

    @Test
    void getItem() {
        when(presetsApiService.getItem(3))
                .thenReturn(Observable.just(new ItemTypeDto(1, "image", "name", 3, "desc", "ball")));
        ItemTypeDto result = presetsService.getItem(3).blockingFirst();
        assertEquals(1, result.id());
        assertEquals("image", result.image());
        assertEquals("name", result.name());
        assertEquals(3, result.price());
        assertEquals("desc", result.description());
        assertEquals("ball", result.use());
        verify(presetsApiService).getItem(3);
    }

    @Test
    void getItemImage() {
        when(presetsApiService.getItemImage(3))
                .thenReturn(Observable.just(responseBody));
        Image result = presetsService.getItemImage(3).blockingFirst();
        assertNull(result.getUrl());
        verify(presetsApiService).getItemImage(3);
    }

    @Test
    void getImage() {
        when(presetsApiService.getTileset(tileSet.image()))
                .thenReturn(Observable.just(responseBody));
        Image result = presetsService.getImage(tileSet).blockingFirst();
        assertNull(result.getUrl());
        verify(presetsApiService).getTileset(tileSet.image());
    }

    @Test
    void getTileViewPort() {
        Rectangle2D result = presetsService.getTileViewPort(1, tileSet);
        assertEquals(0, result.getMinX());
        assertEquals(0, result.getMinY());
        assertEquals(1, result.getWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void getMonsterType() {
        when(presetsApiService.getMonsterType(1))
                .thenReturn(Observable.just(new MonsterTypeDto(1, "name", "image", List.of("a"), "as")));
        MonsterTypeDto result = presetsService.getMonsterType(1).blockingFirst();
        assertEquals(1, result.id());
        assertEquals("name", result.name());
        assertEquals("image", result.image());
        assertEquals(List.of("a"), result.type());
        assertEquals("as", result.description());
        verify(presetsApiService).getMonsterType(1);
    }

    @Test
    void getMonsterImage() {
        when(presetsApiService.getMonsterImage(1))
                .thenReturn(Observable.just(responseBody));
        Image result = presetsService.getMonsterImage(1).blockingFirst();
        assertNull(result.getUrl());
        verify(presetsApiService).getMonsterImage(1);
    }

    @Test
    void getAllBeasts() {
        when(presetsApiService.getMonsters())
                .thenReturn(Observable.just(List.of(new MonsterTypeDto(1, "name", "image", List.of("a"), "as"))));
        List<MonsterTypeDto> result = presetsService.getAllBeasts().blockingFirst();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).id());
        assertEquals("name", result.get(0).name());
        assertEquals("image", result.get(0).image());
        assertEquals(List.of("a"), result.get(0).type());
        assertEquals("as", result.get(0).description());
        verify(presetsApiService).getMonsters();
    }
}