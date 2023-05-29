package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.FriendListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import de.uniks.beastopia.teaml.utils.Prefs;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class FriendListController extends Controller {
    private final List<Controller> subControllers = new ArrayList<>();
    @Inject
    DataCache cache;
    @FXML
    public TextField searchName;
    @FXML
    public Button searchBtn;
    @FXML
    public ScrollPane scrollFriends;
    @FXML
    public VBox friendList;
    @FXML
    public Button showChats;
    @Inject
    Provider<FriendController> friendControllerProvider;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    FriendListService friendListService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;
    LoadingPage loadingPage;
    @Inject
    @SuppressWarnings("unused")
    EventListener eventListener;
    Timer timer = new Timer();
    TimerTask currentTask;
    long taskStamp;
    String lastSearch = null;
    boolean forceUpdate = false;

    @Inject
    public FriendListController() {
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        if (cache.getAllUsers().isEmpty()) {
            disposables.add(friendListService.getUsers()
                    .observeOn(FX_SCHEDULER)
                    .subscribe(users -> {
                        cache.setAllUsers(users);
                        updateUserList();
                        loadingPage.setDone();
                    }));
        } else {
            disposables.add(delay().observeOn(FX_SCHEDULER).subscribe(t -> {
                updateUserList();
                loadingPage.setDone();
            }));
        }

        return loadingPage.parent();
    }

    @Override
    public void destroy() {
        clearSubControllers();
        super.destroy();
    }

    @FXML
    public void showChats() {
        app.show(directMessageControllerProvider.get());
    }

    @FXML
    public void updateUserList() {
        if (lastSearch != null && searchName.getText().equals(lastSearch) && !forceUpdate) {
            return;
        }

        forceUpdate = false;
        clearSubControllers();
        friendList.getChildren().clear();
        System.gc();

        lastSearch = searchName.getText();
        if (lastSearch.isBlank()) {
            getFriends();
        } else {
            getUsers(searchName.getText());
        }
    }

    private void getFriends() {
        List<String> ids = friendListService.getFriendIDs();
        List<User> friends = ids.stream()
                .map(cache::getUser)
                .filter(Objects::nonNull)
                .filter(user -> !user._id().equals(tokenStorage.getCurrentUser()._id()))
                .toList();
        List<User> sortedFriends = sortByPin(friends);
        addUsers(sortedFriends);
    }

    private void getUsers(String name) {
        List<User> users = cache.getAllUsers();
        List<User> filteredUsers = users.stream()
                .filter(user -> user.name().toLowerCase().startsWith(name.toLowerCase()))
                .filter(user -> !user._id().equals(tokenStorage.getCurrentUser()._id()))
                .toList();
        List<User> sortedUsers = sortByPin(filteredUsers);
        addUsers(sortedUsers);
    }

    private void stopTask() {
        if (currentTask != null) {
            currentTask.cancel();
            System.gc();
        }
    }

    private void addUsers(List<User> users) {
        stopTask();

        System.gc();
        taskStamp = System.currentTimeMillis();
        CompositeDisposable timerDisposables = new CompositeDisposable();
        currentTask = new TimerTask() {
            int i = 0;
            final int size = users.size();
            final long stamp = taskStamp;

            @Override
            public void run() {
                if (i >= size || stamp != taskStamp) {
                    timerDisposables.add(delay(50).observeOn(FX_SCHEDULER).subscribe(t -> timerDisposables.dispose()));
                    stopTask();
                    return;
                }

                FriendController controller = friendControllerProvider.get();
                controller.setUser(users.get(i), prefs.isPinned(users.get(i)));
                controller.checkFriend(friendListService.isFriend(users.get(i)));
                controller.init();
                i++;

                timerDisposables.add(delay(50).observeOn(FX_SCHEDULER).subscribe(t -> {
                    if (stamp != taskStamp) {
                        return;
                    }

                    controller.setOnFriendChanged(u -> {
                        searchName.setText("");
                        forceUpdate = true;
                        updateUserList();
                    });
                    controller.setOnPinChanged(u -> {
                        forceUpdate = true;
                        updateUserList();
                    });
                    subControllers.add(controller);
                    friendList.getChildren().add(controller.render());
                }));
            }
        };
        timer.scheduleAtFixedRate(currentTask, 0, 50);
    }

    private List<User> sortByPin(List<User> users) {
        List<User> pinned = new ArrayList<>();
        List<User> unpinned = new ArrayList<>();

        for (User user : users) {
            if (prefs.isPinned(user)) {
                pinned.add(user);
            } else {
                unpinned.add(user);
            }
        }

        pinned.sort(Comparator.comparing(User::name));
        unpinned.sort(Comparator.comparing(User::name));

        pinned.addAll(unpinned);
        return pinned;
    }

    private void clearSubControllers() {
        for (Controller controller : subControllers) {
            controller.destroy();
        }
        subControllers.clear();
        friendList.getChildren().clear();
    }
}
