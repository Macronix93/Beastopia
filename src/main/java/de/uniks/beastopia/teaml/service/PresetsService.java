package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

import javax.inject.Inject;
import java.util.List;

public class PresetsService {
    @Inject
    TokenStorage tokenStorage;

    @Inject
    PresetsApiService presetsApiService;

    public Observable<List<String>> getCharacters() {
        return presetsApiService.getCharacters();
    }

    public Observable<ResponseBody> getSpriteSheet(String fileName) {
        return presetsApiService.getSpriteSheet(fileName);
    }
}