package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DataCache {
    private List<User> users = new ArrayList<>();
    private List<Region> regions = new ArrayList<>();
    private List<Area> areas = new ArrayList<>();

    @Inject
    public DataCache() {
    }

    @SuppressWarnings("unused")
    public void addUser(User user) {
        users.add(user);
    }

    public void setAllUsers(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    @SuppressWarnings("unused")
    public void addUsers(List<User> users) {
        this.users.addAll(users);
    }

    @SuppressWarnings("unused")
    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUser(String id) {
        return users.stream()
                .filter(user -> user._id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = new ArrayList<>(regions);
    }

    public Region getRegion(String id) {
        return regions.stream()
                .filter(region -> region._id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void setAreas(List<Area> areas) {
        this.areas = new ArrayList<>(areas);
    }

    public Area getArea(String id) {
        return areas.stream()
                .filter(area -> area._id().equals(id))
                .findFirst()
                .orElse(null);
    }
    public User getUser(String id) {
        for (User user : allUsers) {
            if (user._id().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
