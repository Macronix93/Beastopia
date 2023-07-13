package de.uniks.beastopia.teaml;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.utils.RingBuffer;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class App extends Application {
    private final static int MAX_SUB_MENUS = 6;
    private MainComponent mainComponent;
    private Stage stage;
    private Controller controller;
    private Scene scene;
    private int windowSizeX = 800;
    private int windowSizeY = 600;
    private final List<Runnable> cleanupTasks = new ArrayList<>();
    private final RingBuffer<Controller> history = new RingBuffer<>(MAX_SUB_MENUS);

    public App() {
        this.mainComponent = de.uniks.beastopia.teaml.DaggerMainComponent.builder().mainApp(this).build();
    }

    public App(MainComponent mainComponent) {
        this.mainComponent = mainComponent;
    }

    public void setHistory(List<Controller> controllers) {
        history.clear();
        for (Controller controller : controllers) {
            history.push(controller);
        }
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
        stage.setWidth(windowSizeX);
        stage.setHeight(windowSizeY);
        stage.setTitle("Beastopia");

        checkJavaVersion();

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            windowSizeX = newValue.intValue();
            if (controller != null) {
                controller.onResize(windowSizeX, windowSizeY);
            }
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            windowSizeY = newValue.intValue();
            if (controller != null) {
                controller.onResize(windowSizeX, windowSizeY);
            }
        });

        scene = new Scene(new Label("Loading..."));
        stage.setScene(scene);

        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/light.css")).toString());
        CSSFX.start();
        setAppIcon();

        stage.show();

        if (mainComponent == null) {
            return;
        }

        mainComponent.themeSettings().updateSceneTheme = theme -> {
            if (theme.equals("dark")) {
                scene.getStylesheets().removeIf(style -> style.endsWith("views/light.css"));
                scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/dark.css")).toString());
            } else {
                scene.getStylesheets().removeIf(style -> style.endsWith("views/dark.css"));
                scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("views/light.css")).toString());
            }
        };

        mainComponent.themeSettings().updateSceneTheme.accept(
                mainComponent.prefs().getTheme()
        );

        //TODO: remove this, restore login screen
        //show(mainComponent.enemyBeastInfoController());
        //show(mainComponent.encounterController());
        show(mainComponent.loginController());
    }

    @Override
    public void stop() {
        cleanupTasks.forEach(Runnable::run);
        history.clear();
    }

    public void showPrevious() {
        if (!history.isEmpty()) {
            controller.destroy();

            controller = history.peek();
            history.pop();

            initAndRender();
        }
    }

    public void show(Controller controller) {
        if (this.controller != null) {
            this.controller.destroy();
            history.push(this.controller);
        }

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

    public void update() {
        if (controller.getTitle() != null) {
            stage.setTitle(controller.getTitle());
        }
        stage.getScene().setRoot(controller.render());
        controller.onResize(windowSizeX, windowSizeY);
    }

    private void setAppIcon() {
        final Image icon = new Image(Objects.requireNonNull(App.class.getResource("assets/bt_icon.png")).toString());
        stage.getIcons().add(icon);
    }

    private void checkJavaVersion() {
        String versionString = System.getProperty("java.version");
        String regex = "17\\..*";
        if (!versionString.matches(regex)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Java Version Warning");
            alert.setHeaderText("Java Version Warning");
            alert.setContentText("You are using Java " + versionString + ".\n" +
                    "This application was tested with Java 17.\n" +
                    "Please use Java 17 to avoid any problems.");
            alert.showAndWait();
        }
    }
}
