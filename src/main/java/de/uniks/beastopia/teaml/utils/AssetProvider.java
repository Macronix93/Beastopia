package de.uniks.beastopia.teaml.utils;

import de.uniks.beastopia.teaml.Main;
import javafx.scene.image.Image;

import java.util.Objects;

public class AssetProvider {

    String statusIconPath = "asset/status/";

    public Image getStatusIcon(String statusName) {
        return new Image(
                Objects.requireNonNull(
                                Main.class.getResource(statusIconPath + statusName + ".png"))
                        .toString()
        );
    }
}
