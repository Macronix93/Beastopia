package de.uniks.beastopia.teaml.controller.menu.social;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.GroupListService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditGroupController extends Controller {

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
    public TextField groupNameField;
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
    private Group group;

    @Inject
    public EditGroupController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();
        groupNameField.setText(group.name());
        for (int i = 0; i < group.members().size(); ++i) {
            User user = cache.getUser(group.members().get(i));
            addedUsersList.add(user);
        }
        updateLists();
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleEditGroup");
    }

    @FXML
    public void updateUserList() {
        clearAvailableUser();

        if (!usernameField.getText().isEmpty()) {
            fillUserIn();
        }
    }

    private void fillUserIn() {
        for (User user : reverse(sortByPin(cache.getAllUsers()))) {
            if (addedUsersList.contains(user)
                    || !user.name().toLowerCase().startsWith(usernameField.getText().toLowerCase())) {
                continue;
            }
            users.getChildren().add(0, showUser(user));
        }
    }

    private void clearAvailableUser() {
        availableUserControllers.clear();
        users.getChildren().clear();
    }

    private Parent showUser(User user) {
        UserController controller = userControllerProvider.get()
                .setIsAdded(true);
        return setUpUser(controller, user);
    }

    private Parent setUpUser(UserController controller, User user) {
        controller.setUser(user);
        controller.setOnUserToggled(this::toggleUser);
        controller.setOnUserPinToggled(this::pinToggelAction);
        controller.init();
        return controller.render();
    }

    private void pinToggelAction(User u) {
        prefs.setPinned(u, !prefs.isPinned(u));
        updateLists();
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
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(u -> u.name().toLowerCase()));
        return pinnedToStart(sortedUsers);
    }

    private List<User> pinnedToStart(List<User> sortedUserNames) {
        List<User> result = new ArrayList<>();
        result.addAll(sortedUserNames.stream().filter(prefs::isPinned).toList());
        result.addAll(sortedUserNames.stream().filter(u -> !prefs.isPinned(u)).toList());
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
        app.showPrevious();
    }

    @Override
    public void destroy() {
        super.destroy();
        addedUserControllers.forEach(Controller::destroy);
        availableUserControllers.forEach(Controller::destroy);
    }

    public EditGroupController setGroup(Group group) {
        this.group = group;
        return this;
    }

    public void editGroup() {
        if (groupNameField.getText().isEmpty()) {
            Dialog.error(resources.getString("groupNameMissing"), resources.getString("enterGroupName"));
            return;
        }
        List<String> userIds = new ArrayList<>();
        for (User user : addedUsersList) {
            userIds.add(user._id());
        }

        userIds.add(tokenStorage.getCurrentUser()._id());

        Group updatedGroup = new Group(group.createdAt(), group.updatedAt(), group._id(), groupNameField.getText(), userIds);

        disposables.add(groupListService.updateGroup(updatedGroup)
                .observeOn(FX_SCHEDULER)
                .subscribe(group -> app.showPrevious(),
                        error -> Dialog.error(error, "error")));
    }
}
