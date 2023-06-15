package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.AchievementsApiService;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.rest.UpdateAchievementDto;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class AchievementsService {

    @Inject
    AchievementsApiService achievementsApiService;

    @Inject
    public AchievementsService() {
    }

    public Observable<List<AchievementsSummary>> getAchievements() {
        return achievementsApiService.getAchievements();
    }

    public Observable<AchievementsSummary> getAchievement(String id) {
        return achievementsApiService.getAchievement(id);
    }

    public Observable<List<Achievement>> getUserAchievements(String userId) {
        return achievementsApiService.getUserAchievements(userId);
    }

    public Observable<Achievement> getUserAchievement(String userId, String id) {
        return achievementsApiService.getUserAchievement(userId, id);
    }

    public Observable<Achievement> updateUserAchievement(String userId, String id, Achievement achievement) {
        return achievementsApiService.updateUserAchievement(userId, id, new UpdateAchievementDto(new Date(), achievement.progress()));
    }

    public Observable<Achievement> deleteUserAchievement(String userId, String id) {
        return achievementsApiService.deleteUserAchievement(userId, id);
    }
}
