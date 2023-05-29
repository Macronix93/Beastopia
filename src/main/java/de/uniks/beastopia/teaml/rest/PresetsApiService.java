package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface PresetsApiService {

    @GET("presets/tilesets/{filename}")
    Observable<ResponseBody> getTileset(@Path("filename") String filename);

    @GET("presets/characters")
    Observable<List<String>> getCharacters();

    @GET("presets/characters/{filename}")
    Observable<ResponseBody> getSpriteSheet(@Path("filename") String fileName);
}