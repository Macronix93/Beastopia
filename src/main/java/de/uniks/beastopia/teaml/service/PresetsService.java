package de.uniks.beastopia.teaml.service;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.rest.*;
import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import okhttp3.ResponseBody;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class PresetsService {
    public static final int PREVIEW_SCALING = 3;
    @Inject
    PresetsApiService presetsApiService;

    @Inject
    public PresetsService() {
    }

    public Observable<List<String>> getCharacters() {
        return presetsApiService.getCharacters();
    }

    public Observable<Image> getCharacterSprites(String fileName, boolean useConstantValues) {
        return presetsApiService.getCharacterSprites(fileName)
                .map((ResponseBody body) ->
                        (useConstantValues ? new Image(body.byteStream(), 384 * PREVIEW_SCALING, 96 * PREVIEW_SCALING, true, false)
                                : new Image(body.byteStream())));
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

    public Observable<MonsterTypeDto> getMonsterType(int type) {
        return presetsApiService.getMonsterType(type);
    }

    public Observable<Image> getMonsterImage(int type) {
        return presetsApiService.getMonsterImage(type)
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }

    public Observable<List<MonsterTypeDto>> getAllBeasts() {
        return presetsApiService.getMonsters();
    }

    public Observable<AbilityDto> getAbility(int id) {
        return presetsApiService.getAbility(id);
    }

    public Observable<List<AbilityDto>> getAbilities() {
        return presetsApiService.getAbilities();
    }

    public Observable<List<Item>> getItems() {
        return presetsApiService.getItems();
    }

    public Observable<Item> getItem(int itemId) {
        return presetsApiService.getItem(itemId);
    }

    public Observable<Image> getItemImage(int itemId) {
        return presetsApiService.getItemImage(itemId)
                .map((ResponseBody body) -> new Image(body.byteStream()));
    }
}