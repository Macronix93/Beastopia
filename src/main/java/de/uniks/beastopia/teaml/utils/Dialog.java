package de.uniks.beastopia.teaml.utils;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.rest.ErrorResponse;
import de.uniks.beastopia.teaml.rest.ValidationErrorResponse;
import javafx.scene.control.Alert;
import retrofit2.HttpException;

import java.io.IOException;
import java.util.Objects;

public class Dialog {
    public static void error(Throwable exception, String dialogHeader) {
        if (exception instanceof HttpException httpError) {
            String message;
            String json;
            try {
                json = Objects.requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                ErrorResponse response = new Gson().fromJson(json, ErrorResponse.class);
                message = response.message();
            } catch (Exception e) {
                try {
                    ValidationErrorResponse response = new Gson().fromJson(json, ValidationErrorResponse.class);
                    message = String.join("\n", response.message());
                } catch (Exception e2) {
                    throw new RuntimeException(e2);
                }
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(dialogHeader);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    public static void error(String dialogHeader, String dialogContent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information");
        alert.setHeaderText(dialogHeader);
        alert.setContentText(dialogContent);
        alert.showAndWait();
    }

    public static void info(String dialogHeader, String dialogContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(dialogHeader);
        alert.setContentText(dialogContent);
        alert.showAndWait();
    }
}
