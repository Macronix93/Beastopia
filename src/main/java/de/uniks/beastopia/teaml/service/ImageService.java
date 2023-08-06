package de.uniks.beastopia.teaml.service;

import de.uniks.beastopia.teaml.Main;
import de.uniks.beastopia.teaml.utils.Prefs;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.Objects;

public class ImageService {

    @Inject
    Prefs prefs;

    @Inject
    ImageService() {
    }

    public Image makeImageBlack (Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage blackImage = new WritableImage(width, height);
        PixelWriter pixelWriter = blackImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = pixelReader.getColor(x, y);

                if (!color.equals(Color.TRANSPARENT)) {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }

        return blackImage;
    }

    public Image makeImageWhite (Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage whiteImage = new WritableImage(width, height);
        PixelWriter pixelWriter = whiteImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = pixelReader.getColor(x, y);

                if (!color.equals(Color.TRANSPARENT)) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                }
            }
        }

        return whiteImage;
    }

    public Image getBanner() {
        if (prefs.getTheme().equals("light")) {
            return new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/beastopia_banner.png")));
        } else {
            return makeImageWhite(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/beastopia_banner.png"))));
        }
    }

    public Image getCompanyLogo() {
        if (prefs.getTheme().equals("light")) {
            return new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/DeadBirdsSociety_Logo_tr.PNG")));
        } else {
            return makeImageWhite(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/DeadBirdsSociety_Logo_tr.PNG"))));
        }
    }
}