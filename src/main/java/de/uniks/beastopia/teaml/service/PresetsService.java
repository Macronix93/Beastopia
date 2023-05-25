package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.presets.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
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
        return presetsApiService.getSpriteSheet(fileName)
                .map(res -> ResponseBody.create(MediaType.parse("image/png"), res.bytes()));
    }
}
