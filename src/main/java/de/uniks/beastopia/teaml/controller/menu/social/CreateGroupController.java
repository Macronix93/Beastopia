package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CreateGroupController extends Controller {

    private final List<User> addedUsersList = new ArrayList<>();
    private final List<Controller> addedUserControllers = new ArrayList<>();
    private final List<Controller> availableUserControllers = new ArrayList<>();
    @FXML
    public TextField usernameField;
    @FXML
    public VBox users;
    @FXML
    public VBox addedUsers;
    @FXML
    public Button createGrpButton;
    @FXML
    public Button backButton;
    @FXML
    public TextField groupNameField;
    @Inject
    Provider<DirectMessageController> directMessageControllerProvider;
    @Inject
    Provider<UserController> userControllerProvider;
    @Inject
    GroupListService groupListService;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;

    @Inject
    public CreateGroupController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("titleNewGroup");
    }

    @FXML
    public void updateUserList() {
        availableUserControllers.clear();
        users.getChildren().clear();

        if (usernameField.getText().isEmpty()) {
            return;
        }

        for (User user : reverse(sortByPin(cache.getAllUsers()))) {
            if (addedUsersList.contains(user) || !user.name().toLowerCase().startsWith(usernameField.getText().toLowerCase())) {
                continue;
            }
            users.getChildren().add(0, showUser(user));
        }
    }

    private Parent showUser(User user) {
        UserController controller = userControllerProvider.get()
                .setIsAdded(false);
        controller.setUser(user);
        controller.setOnUserToggled(this::toggleUser);
        controller.setOnUserPinToggled(u -> {
            prefs.setPinned(u, !prefs.isPinned(u));
            updateLists();
        });
        controller.init();
        return controller.render();
    }

    @FXML
    public void updateAddedUserList() {
        addedUserControllers.clear();
        addedUsers.getChildren().clear();

        for (User user : reverse(sortByPin(addedUsersList))) {
            addedUsers.getChildren().add(0, showUser(user));
        }
    }

    private void toggleUser(User user) {
        if (addedUsersList.contains(user)) {
            addedUsersList.remove(user);
        } else {
            addedUsersList.add(user);
        }

        updateLists();
    }

    private void updateLists() {
        updateUserList();
        updateAddedUserList();
    }

    private List<User> sortByPin(List<User> users) {
        List<User> result = new ArrayList<>();
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(u -> u.name().toLowerCase()));
        result.addAll(sortedUsers.stream().filter(prefs::isPinned).toList());
        result.addAll(sortedUsers.stream().filter(u -> !prefs.isPinned(u)).toList());
        return result;
    }

    private static List<User> reverse(List<User> users) {
        List<User> result = new ArrayList<>();
        for (int i = users.size() - 1; i >= 0; i--) {
            result.add(users.get(i));
        }
        return result;
    }

    @FXML
    public void back() {
        app.show(directMessageControllerProvider.get());
    }

    public void createGroup() {
        if (groupNameField.getText().isEmpty()) {
            Dialog.error(resources.getString("groupNameMissing"), resources.getString("enterGroupName"));
            return;
        }
        List<String> userIds = new ArrayList<>();
        for (User user : addedUsersList) {
            userIds.add(user._id());
        }

        userIds.add(tokenStorage.getCurrentUser()._id());

        disposables.add(groupListService.addGroup(groupNameField.getText(), userIds)
                .observeOn(FX_SCHEDULER)
                .subscribe(group -> {
                    DirectMessageController directMessageController = directMessageControllerProvider.get();
                    app.show(directMessageController);
                }, error -> Dialog.error(error, "error")));

    }

    @Override
    public void destroy() {
        super.destroy();
        addedUserControllers.forEach(Controller::destroy);
        availableUserControllers.forEach(Controller::destroy);
    }
}
