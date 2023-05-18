package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.service.AuthService;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {
    private final MainComponent mainComponent;
    private Stage stage;
    private Controller controller;

    public App() {
        this.mainComponent = DaggerMainComponent.builder().mainApp(this).build();
    }

    public App(MainComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Beastopia");

        Scene scene = new Scene(new Label("Loading..."));
        stage.setScene(scene);

        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/summer.css")).toString());
        CSSFX.start(scene);

        stage.show();

        if (mainComponent == null) {
            return;
        }

        final AuthService authService = mainComponent.authService();
        if (mainComponent.prefs().isRememberMe()) {
            //noinspection ResultOfMethodCallIgnored
            authService.refresh().subscribe(
                    lr -> Platform.runLater(() -> show(mainComponent.menuController())),
                    error -> Platform.runLater(() -> show(mainComponent.loginController())));
        } else {
            show(mainComponent.loginController());
        }
    }

    @Override
    public void stop() {
        cleanup();
    }

    public void show(Controller controller) {
        cleanup();
        this.controller = controller;
        initAndRender(controller);
    }

    private void initAndRender(Controller controller) {
        controller.init();
        if (controller.getTitle() != null) {
            stage.setTitle(controller.getTitle());
        }
        stage.getScene().setRoot(controller.render());
    }

    private void cleanup() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }
}
