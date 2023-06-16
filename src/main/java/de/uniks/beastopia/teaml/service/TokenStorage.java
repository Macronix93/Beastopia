package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.rest.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenStorage {
    private String accessToken;
    private User currentUser;

    @Inject
    public TokenStorage() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}
