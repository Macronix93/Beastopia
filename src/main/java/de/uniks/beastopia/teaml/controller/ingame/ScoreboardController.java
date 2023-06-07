package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AchievementsService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardController extends Controller {

    @FXML
    private VBox achievements;
    @FXML
    private VBox scoreBoard;
    @Inject
    DataCache cache;
    @Inject
    AchievementsService achievementsService;
    @Inject
    TrainerService trainerService;
    @Inject
    Prefs prefs;
    @Inject
    Provider<ScoreboardUserItemController> scoreBoardUserItemControllerProvider;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<Controller> subControllers = new ArrayList<>();

    @Inject
    public ScoreboardController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        achievements.setVisible(false);
        loadAchievements();
        return parent;
    }

    private void loadAchievements() {
        disposables.add(achievementsService.getAchievements().subscribe(allAchievements ->
                disposables.add(delay().map(t -> disposables.add(trainerService.getAllTrainer(prefs.getRegionID()).subscribe(trainers -> {
                    for (Trainer trainer : trainers) {
                        User user = cache.getAllUsers().stream()
                                .filter(u -> u._id().equals(trainer.user()))
                                .findFirst()
                                .orElse(null);

                        if (user == null) {
                            continue;
                        }

                        Thread.sleep(1000);

                        disposables.add(delay().map(t2 -> {
                            disposables.add(achievementsService.getUserAchievements(user._id())
                                    .observeOn(FX_SCHEDULER)
                                    .subscribe(achievementList -> {
                                        ScoreboardUserItemController controller = scoreBoardUserItemControllerProvider.get()
                                                .setName(user.name())
                                                .setAchievements(achievementList.size())
                                                .setTotalAchievements(allAchievements.size());
                                        scoreBoard.getChildren().add(controller.render());
                                        subControllers.add(controller);
                                    }));
                            return t;
                        }).subscribe());
                    }
                }))).subscribe())));
    }
}
