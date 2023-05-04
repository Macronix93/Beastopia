package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Observable<LoginResult> login(@Body LoginDto dto);

    @POST("auth/refresh")
    Observable<LoginResult> refresh(@Body RefreshDto refreshDto);
}
