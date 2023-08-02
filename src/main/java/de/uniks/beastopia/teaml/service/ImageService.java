package de.uniks.beastopia.teaml.service;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class ImageService {

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
}
