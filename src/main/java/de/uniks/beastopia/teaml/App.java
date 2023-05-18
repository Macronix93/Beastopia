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

        if (controller != null) {
            initAndRender(controller);
            return;
        }

        final MainComponent component = DaggerMainComponent.builder().mainApp(this).build();
        final AuthService authService = component.authService();
        if (component.prefs().isRememberMe()) {
            //noinspection ResultOfMethodCallIgnored
            authService.refresh().subscribe(
                    lr -> Platform.runLater(() -> show(component.menuController())),
                    error -> Platform.runLater(() -> show(component.loginController())));
        } else {
            show(component.loginController());
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
