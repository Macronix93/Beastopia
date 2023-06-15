package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;

public class SoundController extends Controller {
    @Inject
    DataCache cache;
    @Inject
    Prefs prefs;

    private AudioClip sfxPlayer;
    private MediaPlayer bgmPlayer;

    @Inject
    public SoundController() {
    }

    public void play(String fileName) {
        try {
            if (fileName.startsWith("bgm:")) {
                if (bgmPlayer != null) {
                    bgmPlayer.stop();
                }
                Media bgmMedia = new Media(Objects.requireNonNull(Main.class.getResource("assets/sounds/bgm_" + fileName.substring(4) + ".mp3")).toURI().toURL().toString());
                bgmPlayer = new MediaPlayer(bgmMedia);

                double volumePercentage = prefs.getMusicVolume();
                double volume = volumePercentage / 100.0;

                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(volume);
                bgmPlayer.play();
            } else if (fileName.startsWith("sfx:")) {
                sfxPlayer = new AudioClip(Objects.requireNonNull(Main.class.getResource("assets/sounds/sfx_" + fileName.substring(4) + ".mp3")).toURI().toString());

                double volumePercentage = prefs.getSoundVolume();
                double volume = volumePercentage / 100.0;

                sfxPlayer.setVolume(volume);
                sfxPlayer.play();
            }
        } catch (URISyntaxException | NullPointerException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
