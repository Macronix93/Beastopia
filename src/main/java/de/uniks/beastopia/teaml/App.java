package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.controller.Controller;

import de.uniks.beastopia.teaml.controller.RegistrationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class App extends Application {
    private Stage stage;
    private Controller controller = new RegistrationController(this);

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

        if (controller != null) {
            initAndRender(controller);
            return;
        }


        final MainComponent component = DaggerMainComponent.builder().mainApp(this).build();
        //controller = component.loginController();
        //initAndRender(controller);

        //TODO in LoginController @Inject und leerer Konstruktor
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
        stage.getScene().setRoot(controller.render());
    }

    private void cleanup() {
        if (controller != null) {
            controller.destroy();
            controller = null;
        }
    }
}
