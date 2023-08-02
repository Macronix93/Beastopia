package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.TrainerItemsApiService;
import de.uniks.beastopia.teaml.rest.UpdateItemDto;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerItemsServiceTest {

    @Mock
    TrainerItemsApiService trainerItemsApiService;
    @InjectMocks
    TrainerItemsService trainerItemsService;

    @Test
    void updateItem() {
        when(trainerItemsApiService.updateItem(anyString(), anyString(), anyString(), any()))
                .thenReturn(Observable.just(new Item(null, null, "ID", "trainer", 1, 2)));
        UpdateItemDto updateItemDto = new UpdateItemDto(1, 2, null);
        Item result = trainerItemsService.updateItem("rId", "tId", "trade", updateItemDto).blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("trainer", result.trainer());
        assertEquals(1, result.type());
        assertEquals(2, result.amount());
        verify(trainerItemsApiService).updateItem("rId", "tId", "trade", updateItemDto);
    }

    @Test
    void getItems() {
        when(trainerItemsApiService.getItems(anyString(), anyString()))
                .thenReturn(Observable.just(List.of(new Item(null, null, "ID", "trainer", 1, 2))));
        List<Item> result = trainerItemsService.getItems("rId", "tId").blockingFirst();
        assertEquals(1, result.size());
        assertEquals("ID", result.get(0)._id());
        assertEquals("trainer", result.get(0).trainer());
        assertEquals(1, result.get(0).type());
        assertEquals(2, result.get(0).amount());
        verify(trainerItemsApiService).getItems("rId", "tId");
    }

    @Test
    void getItem() {
        when(trainerItemsApiService.getItem(anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new Item(null, null, "ID", "trainer", 1, 2)));
        Item result = trainerItemsService.getItem("rId", "tId", "itemID").blockingFirst();
        assertEquals("ID", result._id());
        assertEquals("trainer", result.trainer());
        assertEquals(1, result.type());
        assertEquals(2, result.amount());
        verify(trainerItemsApiService).getItem("rId", "tId", "itemID");
    }
}