package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.LoginDto;
import de.uniks.beastopia.teaml.model.LoginResult;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Observable<LoginResult> login
            (@Body LoginDto dto);
}
