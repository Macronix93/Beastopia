package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.service.FriendListService;
import javafx.scene.Parent;

import javax.inject.Inject;

public class MenuController extends Controller {

    @Inject
    FriendListService friendListService;

    @Inject
    public MenuController() {
    }

    @Override
    public Parent render() {
        return super.render();
    }
}
