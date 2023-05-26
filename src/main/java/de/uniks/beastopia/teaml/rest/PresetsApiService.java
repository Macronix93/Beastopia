package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PresetsApiService {

    @GET("presets/tilesets/{filename}")
    Observable<ResponseBody> getTileset(@Path("filename") String filename);

}
