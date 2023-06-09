package de.uniks.beastopia.teaml.controller.ingame;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.*;
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
import javafx.util.Pair;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {
    static final double TILE_SIZE = 20;

    @FXML
    public HBox ingame;
    @FXML
    public Pane tilePane;
    @FXML
    private HBox scoreBoardLayout;
    @Inject
    App app;
    @Inject
    AreaService areaService;
    @Inject
    PresetsService presetsService;
    @Inject
    TrainerService trainerService;
    @Inject
    Provider<PauseController> pauseControllerProvider;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;
    @Inject
    TokenStorage tokenStorage;
    @Inject
    Provider<EntityController> entityControllerProvider;
    @Inject
    UDPEventListener udpEventListener;
    @Inject
    EventListener eventListener;

    private Region region;
    private Map map;
    private final List<Pair<TileSetDescription, Pair<TileSet, Image>>> tileSets = new ArrayList<>();
    private int posx = 0;
    private int posy = 0;
    private int width;
    private int height;
    private LoadingPage loadingPage;

    Direction direction;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Parent player;
    EntityController playerController;
    @Inject
    Provider<EntityController> entityControllerProvider;
    @Inject
    UDPEventListener udpEventListener;
    @Inject
    ScoreboardController scoreBoardController;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    Parent scoreBoardParent;

    private LoadingPage loadingPage;
    java.util.Map<EntityController, Parent> otherPlayers = new HashMap<>();

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        scoreBoardController.setOnCloseRequested(() -> scoreBoardLayout.getChildren().remove(scoreBoardParent));
        scoreBoardController.init();
        state.setValue(PlayerState.IDLE);
        playerController = entityControllerProvider.get();
        playerController.playerState().bind(state);
        playerController.setOnTrainerUpdate(trainer -> {
            if (!trainer.area().equals(prefs.getArea()._id())) {
                IngameController controller = ingameControllerProvider.get();
                controller.setRegion(region);
                app.show(controller);
                return;
            }
            posx = trainer.x();
            posy = trainer.y();
            updateOrigin();
        });
    }

    public void setRegion(Region region) {
        prefs.setCurrentRegion(region);
        this.region = region;
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());

        disposables.add(trainerService.getAllTrainer(this.region._id())
                .subscribe(trainers -> {
                    Trainer myTrainer = trainers.stream().filter(t -> t.user().equals(tokenStorage.getCurrentUser()._id())).findFirst().orElseThrow();

                    playerController.setTrainer(myTrainer);
                    playerController.init();

                    cache.setTrainer(myTrainer);
                    posx = myTrainer.x();
                    posy = myTrainer.y();
                    disposables.add(areaService.getAreas(this.region._id()).observeOn(FX_SCHEDULER).subscribe(areas -> {
                        cache.setAreas(areas);
                        Area area = areas.stream().filter(a -> a._id().equals(myTrainer.area())).findFirst().orElseThrow();
                        prefs.setArea(area);
                        this.map = area.map();
                        for (TileSetDescription tileSetDesc : map.tilesets()) {
                            TileSet tileSet = presetsService.getTileset(tileSetDesc).blockingFirst();
                            Image image = presetsService.getImage(tileSet).blockingFirst();
                            tileSets.add(new Pair<>(tileSetDesc, new Pair<>(tileSet, image)));
                        }
                        drawMap();
                        scoreBoardParent = scoreBoardController.render();
                        loadingPage.setDone();
                    }));

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
                    Pair<Pair<TileSet, Image>, Integer> tileSet = findTileSet(id);
                    if (tileSet == null) {
                        continue;
                    }

                    drawTile(x, y, tileSet.getKey().getValue(), presetsService.getTileViewPort(tileSet.getValue(), tileSet.getKey().getKey()));
                }
            }
        }
        updateOrigin();
    }

    private Pair<Pair<TileSet, Image>, Integer> findTileSet(int id) {
        id++;
        for (int i = tileSets.size() - 1; i >= 0; i--) {
            Pair<TileSetDescription, Pair<TileSet, Image>> tileSet = tileSets.get(i);
            if (tileSet.getKey().firstgid() <= id) {
                return new Pair<>(tileSet.getValue(), id - tileSet.getKey().firstgid());
            }
        }
        return null;
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
        player.setTranslateY((posy - 1) * TILE_SIZE);
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
        player.toFront();
        player.setTranslateX(x * TILE_SIZE);
        player.setTranslateY((y - 1) * TILE_SIZE);
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
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            PauseController controller = pauseControllerProvider.get();
            controller.setRegion(region);
            app.show(controller);
            return;
        }

        if (keyEvent.getCode().equals(KeyCode.N)) {
            if (scoreBoardLayout.getChildren().contains(scoreBoardParent)) {
                scoreBoardLayout.getChildren().remove(scoreBoardParent);
            } else {
                scoreBoardLayout.getChildren().add(scoreBoardParent);
            }
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
        scoreBoardController.destroy();
        for (EntityController controller : otherPlayers.keySet()) {
            controller.destroy();
        }
    }
}
