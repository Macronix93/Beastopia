package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.controller.Controller;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App extends Application {
    private MainComponent mainComponent;
    private Stage stage;
    private Controller controller;
    private Scene scene;
    private final List<Runnable> cleanupTasks = new ArrayList<>();

    public App() {
        this.mainComponent = de.uniks.beastopia.teaml.DaggerMainComponent.builder().mainApp(this).build();
    }

    public App(MainComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    public void setMainComponent(MainComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    public Stage getStage() {
        return stage;
    }

    public void addCleanupTask(Runnable task) {
        cleanupTasks.add(task);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Beastopia");

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (controller != null) {
                controller.onResize();
            }
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (controller != null) {
                controller.onResize();
            }
        });

        scene = new Scene(new Label("Loading..."));
        stage.setScene(scene);

        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/summer.css")).toString());
        CSSFX.start(scene);

        stage.show();

        if (mainComponent == null) {
            return;
        }

        mainComponent.themeSettings().updateSceneTheme = theme -> {
            if (theme.equals("dark")) {
                scene.getStylesheets().removeIf(style -> style.endsWith("views/summer.css"));
                scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/dark.css")).toString());
            } else {
                scene.getStylesheets().removeIf(style -> style.endsWith("views/dark.css"));
                scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/summer.css")).toString());
            }
        };

        mainComponent.themeSettings().updateSceneTheme.accept(
                mainComponent.prefs().getTheme()
        );

        show(mainComponent.loginController());
    }

    @Override
    public void stop() {
        cleanupTasks.forEach(Runnable::run);
        cleanup();
    }

    public void show(Controller controller) {
        cleanup();
        this.controller = controller;
        initAndRender();
    }

    private void initAndRender() {
        if (controller == null) {
            return;
        }

        controller.init();
        update();
    }

    private void cleanup() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }

    public void update() {
        if (controller.getTitle() != null) {
            stage.setTitle(controller.getTitle());
        }
        stage.getScene().setRoot(controller.render());
    }
}
