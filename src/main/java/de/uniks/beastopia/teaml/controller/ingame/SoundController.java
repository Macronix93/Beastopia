package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Objects;

@Singleton
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

                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(prefs.getMusicVolume() / 100.0);
                bgmPlayer.play();
            } else if (fileName.startsWith("sfx:")) {
                sfxPlayer = new AudioClip(Objects.requireNonNull(Main.class.getResource("assets/sounds/sfx_" + fileName.substring(4) + ".mp3")).toURI().toString());

                sfxPlayer.setVolume(prefs.getSoundVolume() / 100.0);
                sfxPlayer.play();
            }
        } catch (URISyntaxException | NullPointerException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer = null;
        }
    }

    public void updateVolume() {
        bgmPlayer.setVolume(prefs.getMusicVolume() / 100.0);
    }

    public MediaPlayer getBgmPlayer() {
        return bgmPlayer;
    }
}
