package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public abstract class Controller {
    public void init() {

    }

    public void destroy() {

    }

    public Parent render() {
        return load(getClass().getSimpleName().replace("Controller", ""));
    }

    protected Parent load(String view) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/" + view + ".fxml"));
        loader.setControllerFactory(c -> this);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
