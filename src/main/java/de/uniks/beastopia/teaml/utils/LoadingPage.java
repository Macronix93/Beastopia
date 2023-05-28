package de.uniks.beastopia.teaml.utils;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class LoadingPage {
    private final VBox parent;
    private final Region original;
    private boolean isDone = false;

    public static LoadingPage makeLoadingPage(Parent original) {
        VBox parent = new VBox();
        parent.setFillWidth(true);
        VBox.setVgrow(parent, javafx.scene.layout.Priority.ALWAYS);
        parent.setAlignment(javafx.geometry.Pos.CENTER);

        Label label = new Label("Loading...");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setFont(new javafx.scene.text.Font(20.0));
        parent.getChildren().add(label);
        return new LoadingPage(parent, (Region) original);
    }

    private LoadingPage(VBox box, Region original) {
        this.parent = box;
        this.original = original;
    }

    public Parent parent() {
        return parent;
    }

    public void activate() {
        parent.getChildren().clear();
        parent.getChildren().add(original);

        original.prefWidthProperty().bind(parent.widthProperty());
        original.prefHeightProperty().bind(parent.heightProperty());

        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }
}
