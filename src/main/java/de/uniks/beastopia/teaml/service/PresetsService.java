package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.PresetsApiService;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;

import javax.inject.Inject;
import java.util.List;

public class PresetsService {
    @Inject
    PresetsApiService presetsApiService;

    @Inject
    PresetsService() {
    }

    public Observable<List<String>> getCharacters() {
        return presetsApiService.getCharacters();
    }

    public Observable<Image> getCharacterSprites(String fileName) {
        return presetsApiService.getCharacterSprites(fileName)
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }
}