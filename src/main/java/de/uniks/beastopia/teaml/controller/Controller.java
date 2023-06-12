package de.uniks.beastopia.teaml.controller;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.Main;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public abstract class Controller {

    protected static final Scheduler FX_SCHEDULER = Schedulers.from(Platform::runLater);

    @Inject
    protected ResourceBundle resources;

    @Inject
    protected App app;

    protected final CompositeDisposable disposables = new CompositeDisposable();

    public void init() {

    }

    public void destroy() {
        disposables.dispose();
        FX_SCHEDULER.shutdown();
    }

    public String getTitle() {
        return null;
    }

    @SuppressWarnings("unused")
    public void onDestroy(Runnable action) {
        disposables.add(Disposable.fromRunnable(action));
    }

    public Parent render() {
        List<String> parts = new ArrayList<>(Arrays.stream(getClass().getPackageName().split("\\.")).toList());

        // de.uniks.beastopia.teaml.controller.menu.social -> controller.menu.social
        //noinspection OptionalGetWithoutIsPresent
        while (!parts.stream().findFirst().get().equals("controller")) {
            parts.remove(0);
        }
        // controller.menu.social -> menu.social
        parts.remove(0);
        String path = String.join("/", parts);
        return load(path + "/" + getClass().getSimpleName().replace("Controller", ""));
    }

    protected Observable<Long> delay() {
        return Observable.timer(50, TimeUnit.MILLISECONDS);
    }

    protected Observable<Long> delay(@SuppressWarnings("SameParameterValue") long ms) {
        return Observable.timer(ms, TimeUnit.MILLISECONDS);
    }

    public void onResize(int width, int height) {

    }

    protected Parent load(String view) {
        final FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/" + view + ".fxml"));
        loader.setControllerFactory(c -> this);
        loader.setResources(resources);
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
