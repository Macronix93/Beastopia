package de.uniks.beastopia.teaml.utils;

import de.uniks.beastopia.teaml.rest.User;

import javax.inject.Inject;
import java.util.prefs.Preferences;

public class Prefs {
    @Inject
    Preferences preferences;

    @Inject
    public Prefs() {
    }

    public boolean isPinned(User user) {
        return preferences.getBoolean(user._id() + "_pinned", false);
    }

    public void setPinned(User user, boolean pinned) {
        preferences.putBoolean(user._id() + "_pinned", pinned);
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
}
