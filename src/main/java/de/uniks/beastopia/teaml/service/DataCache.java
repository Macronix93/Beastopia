package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DataCache {
    private List<User> allUsers = new ArrayList<>();

    @Inject
    public DataCache() {
    }

    @SuppressWarnings("unused")
    public void addUser(User user) {
        allUsers.add(user);
    }

    public void setAllUsers(List<User> users) {
        allUsers = new ArrayList<>(users);
    }

    @SuppressWarnings("unused")
    public void addUsers(List<User> users) {
        allUsers.addAll(users);
    }

    @SuppressWarnings("unused")
    public void removeUser(User user) {
        allUsers.remove(user);
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    public User getUser(String id) {
        return allUsers.stream()
                .filter(user -> user._id().equals(id))
                .findFirst()
                .orElse(null);
    }
}
