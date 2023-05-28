package de.uniks.beastopia.teaml.utils;

import com.google.gson.Gson;
import de.uniks.beastopia.teaml.rest.ErrorResponse;
import de.uniks.beastopia.teaml.rest.ValidationErrorResponse;
import javafx.scene.control.Alert;
import retrofit2.HttpException;

import java.util.Objects;

public class Dialog {
    public static void error(Throwable exception, String dialogHeader) {
        if (exception instanceof HttpException httpError) {
            String message;
            try {
                String json = Objects.requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
                ErrorResponse response = new Gson().fromJson(json, ErrorResponse.class);
                message = response.message();
            } catch (Exception e) {
                try {
                    String json = Objects.requireNonNull(Objects.requireNonNull(httpError.response()).errorBody()).string();
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

    public static void info(String dialogHeader, String dialogContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(dialogHeader);
        alert.setContentText(dialogContent);
        alert.showAndWait();
    }
}
