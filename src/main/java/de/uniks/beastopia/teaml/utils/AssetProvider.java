package de.uniks.beastopia.teaml.utils;

import de.uniks.beastopia.teaml.Main;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;

@Singleton
public class AssetProvider {
    @Inject
    public AssetProvider() {
    }

    public ImageView getIcon(String iconType, String filename, int width, int height) {
        ImageView imageView = new ImageView(
                Objects.requireNonNull(
                                Main.class.getResource("assets/" + iconType + "/" + filename + ".png"))
                        .toString()
        );

        imageView.setCache(false);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }
}
