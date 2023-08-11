package de.uniks.beastopia.teaml.controller.ingame.scoreboard;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.ingame.UserInfoController;
import de.uniks.beastopia.teaml.rest.Achievement;
import de.uniks.beastopia.teaml.rest.AchievementsSummary;
import de.uniks.beastopia.teaml.rest.Trainer;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.AchievementsService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    @Inject
    Provider<ScoreboardFilterController> scoreboardFilterControllerProvider;
    @Inject
    Provider<UserInfoController> userInfoControllerProvider;
    private final ObservableList<ScoreboardUserItemController> scoreboardControllers = FXCollections.observableArrayList();
    private Runnable onCloseRequested;
    private boolean clicked = false;
    private boolean clickedFilter = false;
    private UserInfoController userInfoController;
    private ScoreboardFilterController scoreboardFilterController;
    private String selectedUserID = "";
    private List<AchievementsSummary> currentAchievements;

    @Inject
    public ScoreboardController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        achievements.setVisible(clicked);
        loadAchievements();
        return parent;
    }

    private void loadAchievements() {
        disposables.add(achievementsService.getAchievements().subscribe(allAchievements -> {
            currentAchievements = new ArrayList<>(allAchievements);

            for (AchievementsSummary achievement : currentAchievements) {
                switch (achievement.id()) {
                    case "FirstTrainer" ->
                            cache.addAchievementDescription("FirstTrainer", resources.getString("achievementFirstTrainer"));
                    case "FirstRegion" ->
                            cache.addAchievementDescription("FirstRegion", resources.getString("achievementFirstRegion"));
                    case "MoveCharacter" ->
                            cache.addAchievementDescription("MoveCharacter", resources.getString("achievementMoveCharacter"));
                    case "MeetPlayer" ->
                            cache.addAchievementDescription("MeetPlayer", resources.getString("achievementMeetPlayer"));
                    case "MeetAlbert" ->
                            cache.addAchievementDescription("MeetAlbert", resources.getString("achievementMeetAlbert"));
                    case "VisitAllRegions" ->
                            cache.addAchievementDescription("VisitAllRegions", resources.getString("achievementVisitAllRegions"));
                    case "FirstMonster" ->
                        cache.addAchievementDescription("FirstMonster", resources.getString("achievementFirstMonster"));
                    case "TenMonsters" ->
                        cache.addAchievementDescription("TenMonsters", resources.getString("achievementTenMonsters"));
                    case "FiftyMonsters" ->
                        cache.addAchievementDescription("FiftyMonsters", resources.getString("achievementFiftyMonsters"));
                    case "AllMonsters" ->
                        cache.addAchievementDescription("AllMonsters", resources.getString("achievementAllMonsters"));
                    case "FirstCoins" ->
                        cache.addAchievementDescription("FirstCoins", resources.getString("achievementFirstCoins"));
                    case "HundredCoins" ->
                        cache.addAchievementDescription("HundredCoins", resources.getString("achievementHundredCoins"));
                    case "thousandCoins" ->
                        cache.addAchievementDescription("thousandCoins", resources.getString("achievementThousandCoins"));
                    case "hundredThousandCoins" ->
                        cache.addAchievementDescription("hundredThousandCoins", resources.getString("achievementHundredThousandCoins"));
                    default -> cache.addAchievementDescription("NotFound", "NotFound");
                }
            }

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
                                            .setUser(user)
                                            .setAchievements(achievementList.size())
                                            .setOnUserClicked(u -> onUserClicked(u, achievementList.size(), allAchievements.size(), achievementList))
                                            .setTotalAchievements(allAchievements.size())
                                            .setUserAchievements(achievementList);

                                    scoreboardControllers.add(controller);

                                    Parent parent = controller.render();
                                    controller.setParent(parent);

                                    if (scoreboardFilterController != null) {
                                        if (scoreboardFilterController.isFilterApplied()) {
                                            scoreboardFilterController.checkCurrentController(controller);
                                        } else {
                                            scoreBoard.getChildren().add(parent);
                                        }
                                    } else {
                                        scoreBoard.getChildren().add(parent);
                                    }

                                    HBox.setHgrow(parent, Priority.ALWAYS);
                                }));
                        return t;
                    }).subscribe());
                }
            }))).subscribe());
        }));
    }

    public void handleKeyEvent(KeyEvent event) {
        if (event.getCode().equals(KeyCode.N)) {
            onCloseRequested.run();
        }
    }

    public void setOnCloseRequested(Runnable onCloseRequested) {
        this.onCloseRequested = onCloseRequested;
    }

    private void onUserClicked(String userId, int noOfAchievements, int totalAchievements, List<Achievement> userAchievements) {
        User user = cache.getAllUsers().stream()
                .filter(u -> u._id().equals(userId))
                .findFirst()
                .orElseThrow();

        if (selectedUserID.equals(user._id())) {
            achievements.setVisible(!clicked);
            clicked = !clicked;
            return;
        }

        achievements.setVisible(true);
        clicked = true;
        selectedUserID = user._id();

        if (userInfoController != null) {
            userInfoController.destroy();
        }

        if (scoreboardFilterController != null) {
            clickedFilter = false;
            scoreboardFilterController.destroy();
        }

        userInfoController = userInfoControllerProvider.get()
                .setName(user.name())
                .setAchievements(noOfAchievements)
                .setTotalAchievements(totalAchievements)
                .setUserAchievements(userAchievements)
                .setUserAvatar(cache.getImageAvatar(user));

        Parent parent = userInfoController.render();
        achievements.getChildren().clear();
        achievements.getChildren().add(parent);
    }

    public void showFilterOptions() {
        achievements.setVisible(!clickedFilter);
        clickedFilter = !clickedFilter;

        if (userInfoController != null) {
            userInfoController.destroy();
            clicked = false;
        }

        if (scoreboardFilterController != null) {
            if (scoreboardFilterController.isFilterApplied()) {
                scoreboardFilterController.removeFilter();
            }
            scoreboardFilterController.destroy();
        }

        scoreboardFilterController = scoreboardFilterControllerProvider.get()
                .setCurrentAchievements(currentAchievements)
                .setSubControllers(scoreboardControllers)
                .setParentPane(scoreBoard);

        Parent parent = scoreboardFilterController.render();
        achievements.getChildren().clear();
        achievements.getChildren().add(parent);
    }

    @Override
    public void destroy() {
        super.destroy();
        scoreboardControllers.forEach(Controller::destroy);
        if (userInfoController != null) {
            userInfoController.destroy();
        }
    }
}
