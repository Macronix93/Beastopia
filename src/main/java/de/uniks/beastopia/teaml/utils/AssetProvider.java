package de.uniks.beastopia.teaml.utils;

import de.uniks.beastopia.teaml.Main;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class AssetProvider {

    final String statusIconPath = "asset/status/";
    final String buttonIconPath = "assets/buttons/";

    @Inject
    public AssetProvider() {
    }

    public Image getStatusIcon(String statusName) {
        return new Image(
                Objects.requireNonNull(
                                Main.class.getResource(statusIconPath + statusName + ".png"))
                        .toString()
        );
    }

    public ImageView getButtonImageView(String statusName) {
        ImageView imageView = new ImageView(
                Objects.requireNonNull(
                                Main.class.getResource(buttonIconPath + statusName + ".png"))
                        .toString()
        );
        imageView.setCache(false);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }
}
