package de.uniks.beastopia.teaml.utils;

import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.Group;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.User;
import de.uniks.beastopia.teaml.service.DataCache;
import javafx.geometry.Point2D;

import javax.inject.Inject;
import java.util.Locale;
import java.util.prefs.Preferences;

public class Prefs {
    @Inject
    Preferences preferences;

    @Inject
    DataCache cache;

    @Inject
    public Prefs() {
    }

    public boolean isPinned(User user) {
        return preferences.getBoolean(user._id() + "_pinned", false);
    }

    public boolean isPinned(Group group) {
        return preferences.getBoolean(group._id() + "_pinned", false);
    }

    public void setPinned(User user, boolean pinned) {
        preferences.putBoolean(user._id() + "_pinned", pinned);
    }

    public void setPinned(Group group, boolean pinned) {
        preferences.putBoolean(group._id() + "_pinned", pinned);
    }

    public boolean isRememberMe() {
        return preferences.get("rememberMe", null) != null;
    }

    public String getRememberMeToken() {
        return preferences.get("rememberMe", null);
    }

    public void setRememberMe(String refreshToken) {
        preferences.put("rememberMe", refreshToken);
    }

    public void clearRememberMe() {
        preferences.remove("rememberMe");
    }

    public String getTheme() {
        return preferences.getBoolean("DarkTheme", false) ? "dark" : "light";
    }

    public void setTheme(String theme) {
        preferences.putBoolean("DarkTheme", theme.equals("dark"));
    }

    public String getLocale() {
        String userLocale = Locale.getDefault().toLanguageTag();
        if (!userLocale.equals("en-EN") && !userLocale.equals("de-DE")) {
            userLocale = "en-EN";
        }

        return preferences.get("locale", userLocale);
    }

    public void setLocale(String locale) {
        preferences.put("locale", locale);
    }

    public void setCurrentRegion(Region region) {
        preferences.put("region", region._id());
    }

    public String getRegionID() {
        return preferences.get("region", null);
    }

    public void setArea(Area area) {
        preferences.put("area", area._id());
    }

    public Area getArea() {
        return cache.getArea(preferences.get("area", null));
    }

    public void setPosition(Point2D position) {
        preferences.putDouble("positionX", position.getX());
        preferences.putDouble("positionY", position.getY());
    }

    public void setMusicVolume(double volume) {
        preferences.putDouble("musicVolume", volume);
    }

    public double getMusicVolume() {
        return preferences.getDouble("musicVolume", 50.0);
    }

    public void setSoundVolume(double volume) {
        preferences.putDouble("soundVolume", volume);
    }

    public double getSoundVolume() {
        return preferences.getDouble("soundVolume", 50.0);
    }

    public void addVisitedArea(String areas) {
        preferences.put("visitedAreas", areas);
    }

    public String getVisitedAreas() {
        return preferences.get("visitedAreas", null);
    }
}
