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
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setTitle("Beastopia");

        final Scene scene = new Scene(new Label("Loading..."));
        stage.setScene(scene);

        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/styles.css")).toString());
        CSSFX.start(scene);

        stage.show();

        if (controller != null) {
            initAndRender();
            return;
        }

        final MainComponent component = DaggerMainComponent.builder().mainApp(this).build();
        final AuthService authService = component.authService();
        if (authService.isRememberMe()) {
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
        initAndRender();
    }

    private void initAndRender() {
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
