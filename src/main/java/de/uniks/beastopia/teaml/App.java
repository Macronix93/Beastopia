package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {
    private Stage stage;
    private Controller controller;

    public App() {

    }

    public App(Controller controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Beastopia");

        final Scene scene = new Scene(new Label("Loading..."));
        stage.setScene(scene);

        stage.show();
    }
}
