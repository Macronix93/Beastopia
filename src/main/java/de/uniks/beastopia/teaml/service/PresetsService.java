package de.uniks.beastopia.teaml.service;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.rest.MonsterTypeDto;
import de.uniks.beastopia.teaml.rest.PresetsApiService;
import de.uniks.beastopia.teaml.rest.TileSet;
import de.uniks.beastopia.teaml.rest.TileSetDescription;
import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class PresetsService {
    @Inject
    PresetsApiService presetsApiService;

    @Inject
    public PresetsService() {
    }

    public Observable<List<String>> getCharacters() {
        return presetsApiService.getCharacters();
    }

    public Observable<Image> getCharacterSprites(String fileName) {
        return presetsApiService.getCharacterSprites(fileName)
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }

    public Observable<Image> getImage(TileSet tileSet) {
        return presetsApiService.getTileset(tileSet.image())
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }

    public Observable<TileSet> getTileset(TileSetDescription description) {
        return presetsApiService.getTileset(new File(description.source()).getName())
                .map((ResponseBody body) -> new Gson().fromJson(body.string(), TileSet.class));
    }

    public Rectangle2D getTileViewPort(int id, TileSet tileSet) {
        int columns = tileSet.columns();
        int tileSize = tileSet.tileheight();
        int x = (id - 1) % columns;
        int y = (id - 1) / columns;
        return new Rectangle2D(x * tileSize, y * tileSize, tileSize, tileSize);
    }

    public Observable<MonsterTypeDto> getMonsterType(String name) {
        return presetsApiService.getMonsterType(name);
    }

    public Observable<Image> getMonsterImage(String name) {
        return presetsApiService.getMonsterImage(name)
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }
}