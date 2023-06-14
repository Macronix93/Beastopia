package de.uniks.beastopia.teaml.rest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.*;

import java.util.List;

public interface AchievementsApiService {

    @GET("achievements")
    Observable<List<AchievementsSummary>> getAchievements();

    @GET("achievements/{id}")
    Observable<AchievementsSummary> getAchievement(@Path("id") String id);

    @GET("users/{user}/achievements")
    Observable<List<Achievement>> getUserAchievements(@Path("user") String user);

    @GET("users/{user}/achievements/{id}")
    Observable<Achievement> getUserAchievement(@Path("user") String user, @Path("id") String id);

    @PUT("users/{user}/achievements/{id}")
    Observable<Achievement> updateUserAchievement(@Path("user") String user, @Path("id") String id, @Body UpdateAchievementDto achievementDto);

    @DELETE
    Observable<Achievement> deleteUserAchievement(String user, String id);
}
