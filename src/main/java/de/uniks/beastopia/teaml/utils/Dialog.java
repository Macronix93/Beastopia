package de.uniks.beastopia.teaml.utils;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.rest.ErrorResponse;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import retrofit2.HttpException;

import java.util.Objects;

public class Dialog {
    public static void error(Throwable exception, String dialogHeader) {
        Platform.runLater(() -> {
            if (exception instanceof HttpException httpError) {
                String message;
                try {
                    String json = Objects.requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
                    ErrorResponse response = new Gson().fromJson(json, ErrorResponse.class);
                    message = response.message();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(dialogHeader);
                alert.setContentText(message);
                alert.showAndWait();
            }
        });
    }

    public static void info(String dialogHeader, String dialogContent) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(dialogHeader);
            alert.setContentText(dialogContent);
            alert.showAndWait();
        });
    }
}
