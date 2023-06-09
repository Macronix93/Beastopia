package de.uniks.beastopia.teaml.controller.ingame;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.AreaService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.sockets.EventListener;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import de.uniks.beastopia.teaml.utils.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;

public class IngameController extends Controller {
    static final double TILE_SIZE = 20;

    @FXML
    public HBox ingame;
    @FXML
    public Pane tilePane;
    @Inject
    App app;
    @Inject
    AreaService areaService;
    @Inject
    TrainerService trainerService;
    @Inject
    PresetsService presetsService;
    @Inject
    Provider<PauseController> pauseControllerProvider;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;
    @Inject
    Provider<EntityController> entityControllerProvider;
    @Inject
    UDPEventListener udpEventListener;
    @Inject
    EventListener eventListener;
    @Inject
    Provider<MapController> mapControllerProvider;

    private Region region;
    private Image image;
    private Map map;
    private TileSet tileSet;
    private int posx = 0;
    private int posy = 0;
    private int width;
    private int height;
    private LoadingPage loadingPage;

    Direction direction;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Parent player;
    EntityController playerController;
    java.util.Map<EntityController, Parent> otherPlayers = new HashMap<>();

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        state.setValue(PlayerState.IDLE);
        playerController = entityControllerProvider.get();
        playerController.setTrainer(cache.getTrainer());
        playerController.playerState().bind(state);
        playerController.setOnTrainerUpdate(trainer -> {
            posx = trainer.x();
            posy = trainer.y();
            updateOrigin();
        });
        playerController.init();
    }

    public void setRegion(Region region) {
        prefs.setRegion(region);
        this.region = region;
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        disposables.add(areaService.getAreas(region._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(areas -> {
                    cache.setAreas(areas);
                    if (prefs.getArea() == null) {
                        Area area = areas.stream().filter(a -> a._id().equals(region.spawn().area())).findFirst().orElse(null);
                        if (area == null) {
                            loadingPage.setDone();
                            return;
                        }
                        prefs.setArea(area);
                        posx = region.spawn().x();
                        posy = region.spawn().y();
                        this.map = area.map();
                    } else {
                        posx = (int) prefs.getPosition().getX();
                        posy = (int) prefs.getPosition().getY();
                        this.map = prefs.getArea().map();
                    }
                    this.tileSet = presetsService.getTileset(map.tilesets().get(0)).blockingFirst();
                    this.image = presetsService.getImage(tileSet).blockingFirst();

                    drawMap();

                    disposables.add(trainerService.getAllTrainer(region._id()).observeOn(FX_SCHEDULER).subscribe(trainers -> {
                        cache.setTrainers(trainers);
                        for (Trainer trainer : trainers) {
                            if (trainer._id().equals(cache.getTrainer()._id())) {
                                continue;
                            }
                            createRemotePlayer(trainer);
                        }
                        disposables.add(eventListener.listen(
                                        "regions." + this.region._id() + ".trainers.*.created",
                                        Trainer.class)
                                .observeOn(FX_SCHEDULER)
                                .subscribe(event -> this.createRemotePlayer(event.data()),
                                        error -> Dialog.error(error, resources.getString("getAllTrainerError"))));
                        disposables.add(eventListener.listen(
                                        "regions." + this.region._id() + ".trainers.*.deleted",
                                        Trainer.class)
                                .observeOn(FX_SCHEDULER)
                                .subscribe(event -> this.removeRemotePlayer(event.data()),
                                        error -> Dialog.error(error, resources.getString("getAllTrainerError"))));
                    }, error -> Dialog.error(error, resources.getString("getAllTrainerError"))));

                    loadingPage.setDone();
                }));

        return loadingPage.parent();
    }

    private void createRemotePlayer(Trainer trainer) {
        if (!prefs.getArea()._id().equals(trainer.area())) {
            return;
        }

        System.out.println("trainer at pos: " + trainer.x() + " " + trainer.y());
        EntityController controller = entityControllerProvider.get();
        ObjectProperty<PlayerState> ps = new SimpleObjectProperty<>();
        controller.playerState().bind(ps);
        ps.setValue(PlayerState.IDLE);
        controller.setTrainer(trainer);
        controller.setOnTrainerUpdate(moveDto -> {
            if (!isBeingRendered(moveDto._id()) && moveDto.area().equals(prefs.getArea()._id())) {
                revealRemotePlayer(cache.getTrainer(moveDto._id()));
            } else if (isBeingRendered(moveDto._id()) && !moveDto.area().equals(prefs.getArea()._id())) {
                hideRemotePlayer(cache.getTrainer(moveDto._id()));
            } else {
                moveRemotePlayer(controller, moveDto.x(), moveDto.y());
            }
        });
        controller.init();
        Parent parent = drawRemotePlayer(controller, trainer.x(), trainer.y());
        otherPlayers.put(controller, parent);

        if (!prefs.getArea()._id().equals(trainer.area())) {
            hideRemotePlayer(trainer);
        }
    }

    private void removeRemotePlayer(Trainer trainer) {
        EntityController trainerController = null;
        for (EntityController controller : otherPlayers.keySet()) {
            if (controller.getTrainer()._id().equals(trainer._id())) {
                trainerController = controller;
                break;
            }
        }

        if (trainerController == null) {
            return;
        }

        tilePane.getChildren().remove(otherPlayers.get(trainerController));
        trainerController.destroy();
        otherPlayers.remove(trainerController);
    }

    private void hideRemotePlayer(Trainer trainer) {
        EntityController trainerController = null;
        for (EntityController controller : otherPlayers.keySet()) {
            if (controller.getTrainer()._id().equals(trainer._id())) {
                trainerController = controller;
                break;
            }
        }

        if (trainerController == null) {
            return;
        }

        tilePane.getChildren().remove(otherPlayers.get(trainerController));
    }

    private void revealRemotePlayer(Trainer trainer) {
        EntityController trainerController = null;
        for (EntityController controller : otherPlayers.keySet()) {
            if (controller.getTrainer()._id().equals(trainer._id())) {
                trainerController = controller;
                break;
            }
        }

        if (trainerController == null) {
            return;
        }

        tilePane.getChildren().add(otherPlayers.get(trainerController));
    }

    private boolean isBeingRendered(String trainer) {
        for (EntityController controller : otherPlayers.keySet()) {
            if (controller.getTrainer()._id().equals(trainer)) {
                return tilePane.getChildren().contains(otherPlayers.get(controller));
            }
        }
        return false;
    }

    private void drawMap() {
        drawPlayer(posx, posy);
        for (Layer layer : map.layers()) {
            if (layer.chunks() == null) {
                continue;
            }

            for (Chunk chunk : layer.chunks()) {
                int chunkX = chunk.x();
                int chunkY = chunk.y();
                int index = 0;
                for (int id : chunk.data()) {
                    int x = index % chunk.width() + chunkX;
                    int y = index / chunk.width() + chunkY;
                    index++;
                    drawTile(x, y, image, presetsService.getTileViewPort(id, tileSet));
                }
            }
        }

        updateOrigin();
    }

    private void drawTile(int x, int y, Image image, Rectangle2D viewPort) {
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.setSmooth(true);
        view.setImage(image);
        view.setFitWidth(TILE_SIZE + 1);
        view.setFitHeight(TILE_SIZE + 1);
        view.setViewport(viewPort);
        view.setTranslateX(x * TILE_SIZE);
        view.setTranslateY(y * TILE_SIZE);
        tilePane.getChildren().add(view);
    }

    public void setOrigin(int tilex, int tiley) {
        double parentWidth = width;
        double parentHeight = height;

        double originX = parentWidth / 2 - TILE_SIZE / 2;
        double originY = parentHeight / 2 - TILE_SIZE / 2;

        double tilePaneTranslationX = originX - tilex * TILE_SIZE;
        double tilePaneTranslationY = originY - tiley * TILE_SIZE;

        tilePane.setTranslateX(tilePaneTranslationX);
        tilePane.setTranslateY(tilePaneTranslationY);

        movePlayer(tilex, tiley);
        prefs.setPosition(new Point2D(tilex, tiley));
    }

    public void updateOrigin() {
        setOrigin(posx, posy);
    }

    private void drawPlayer(int posx, int posy) {
        tilePane.getChildren().remove(player);
        player = playerController.render();
        player.setTranslateX(posx * TILE_SIZE);
        player.setTranslateY(posy * TILE_SIZE);
        tilePane.getChildren().add(player);
        player.toFront();
    }

    private Parent drawRemotePlayer(EntityController controller, int posx, int posy) {
        Parent parent = controller.render();
        parent.setTranslateX(posx * TILE_SIZE);
        parent.setTranslateY(posy * TILE_SIZE);
        tilePane.getChildren().add(parent);
        parent.toFront();
        return parent;
    }

    private void movePlayer(int x, int y) {
        drawPlayer(x, y);
        player.toFront();
        player.setTranslateX(x * TILE_SIZE);
        player.setTranslateY(y * TILE_SIZE);
    }

    private void moveRemotePlayer(EntityController controller, int x, int y) {
        Parent remotePlayer = otherPlayers.get(controller);
        remotePlayer.toFront();
        remotePlayer.setTranslateX(x * TILE_SIZE);
        remotePlayer.setTranslateY(y * TILE_SIZE);
    }

    @Override
    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;
        if (loadingPage.isDone()) {
            updateOrigin();
        }
    }

    private void updateTrainerPos(Direction direction) {
        Trainer trainer = cache.getTrainer();
        JsonObject data = new JsonObject();
        data.add("_id", new JsonPrimitive(trainer._id()));
        data.add("area", new JsonPrimitive(trainer.area()));
        data.add("x", new JsonPrimitive(posx));
        data.add("y", new JsonPrimitive(posy));
        data.add("direction", new JsonPrimitive(direction.ordinal()));

        JsonObject message = new JsonObject();
        message.add("event", new JsonPrimitive("areas." + trainer.area() + ".trainers." + trainer._id() + ".moved"));
        message.add("data", data);

        udpEventListener.send(message.toString());
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.M)) {
            MapController map = mapControllerProvider.get();
            map.setRegion(region);
            map.setBackController(this);
            app.show(map);
        }

        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            PauseController controller = pauseControllerProvider.get();
            controller.setRegion(region);
            app.show(controller);
        }

        boolean moved = false;
        if (keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.W)) {
            posy--;
            direction = Direction.UP;
            moved = true;
        } else if (keyEvent.getCode().equals(KeyCode.DOWN) || keyEvent.getCode().equals(KeyCode.S)) {
            posy++;
            direction = Direction.DOWN;
            moved = true;
        } else if (keyEvent.getCode().equals(KeyCode.LEFT) || keyEvent.getCode().equals(KeyCode.A)) {
            posx--;
            direction = Direction.LEFT;
            moved = true;
        } else if (keyEvent.getCode().equals(KeyCode.RIGHT) || keyEvent.getCode().equals(KeyCode.D)) {
            posx++;
            direction = Direction.RIGHT;
            moved = true;
        }

        if (moved) {
            state.setValue(PlayerState.WALKING);
            updateTrainerPos(direction);
            updateOrigin();
        }
    }

    @FXML
    public void setIdleState() {
        state.setValue(PlayerState.IDLE);
        drawPlayer(posx, posy);
    }

    @SuppressWarnings("unused")
    private void updateRemoteTrainerPos(Direction direction) {

    }


    @Override
    public String getTitle() {
        return resources.getString("titleIngame");
    }

    @Override
    public void destroy() {
        super.destroy();
        playerController.destroy();
        for (EntityController controller : otherPlayers.keySet()) {
            controller.destroy();
        }
    }
}
