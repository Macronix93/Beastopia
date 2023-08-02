package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Item;
import de.uniks.beastopia.teaml.rest.TrainerItemsApiService;
import de.uniks.beastopia.teaml.rest.UpdateItemDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.List;

public class TrainerItemsService {

    @Inject
    TrainerItemsApiService trainerItemsApiService;

    @Inject
    public TrainerItemsService() {
    }

    public Observable<Item> updateItem(String regionId, String trainerId, String action, UpdateItemDto updateItemDto) {
        return trainerItemsApiService.updateItem(regionId, trainerId, action, updateItemDto);
    }

    public Observable<List<Item>> getItems(String regionId, String trainerId) {
        return trainerItemsApiService.getItems(regionId, trainerId);
    }

    public Observable<Item> getItem(String regionId, String trainerId, String ItemId) {
        return trainerItemsApiService.getItem(regionId, trainerId, ItemId);
    }
}
