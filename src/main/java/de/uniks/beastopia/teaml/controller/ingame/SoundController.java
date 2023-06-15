package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Prefs;

import javax.inject.Inject;

public class SoundController extends Controller {
    @Inject
    DataCache cache;
    @Inject
    Prefs prefs;

    @Inject
    public SoundController() {
    }

    @Override
    public void init() {
    }
}
