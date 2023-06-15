package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.media.AudioClip;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.Objects;

public class SoundController extends Controller {
    @Inject
    DataCache cache;
    @Inject
    Prefs prefs;

    private AudioClip sfxPlayer;

    @Inject
    public SoundController() {
    }

    @Override
    public void init() {
        super.init();
    }

    public void play(String fileName) {
        try {
            sfxPlayer = new AudioClip(Objects.requireNonNull(Main.class.getResource("assets/sounds/" + fileName)).toURI().toString());

            double volumePercentage = prefs.getSoundVolume();
            double volume = volumePercentage / 100.0;

            sfxPlayer.setVolume(volume);
            sfxPlayer.play();
        } catch (URISyntaxException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }
}
