package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.rest.Area;
import de.uniks.beastopia.teaml.rest.Chunk;
import de.uniks.beastopia.teaml.rest.Layer;
import de.uniks.beastopia.teaml.rest.Map;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.rest.TileSet;
import de.uniks.beastopia.teaml.service.AreaService;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.sockets.UDPEventListener;
import de.uniks.beastopia.teaml.utils.Direction;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import de.uniks.beastopia.teaml.utils.PlayerState;
import de.uniks.beastopia.teaml.utils.Prefs;
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
    PresetsService presetsService;
    @Inject
    Provider<PauseController> pauseControllerProvider;
    @Inject
    Prefs prefs;
    @Inject
    DataCache cache;
    private Region region;
    private Image image;
    private Map map;
    private TileSet tileSet;
    private int posx = 0;
    private int posy = 0;
    private int width;
    private int height;
    Direction direction;
    ObjectProperty<PlayerState> state = new SimpleObjectProperty<>();
    Parent player;
    EntityController playerController;
    @Inject
    Provider<EntityController> entityControllerProvider;
    @Inject
    UDPEventListener udpEventListener;

    private LoadingPage loadingPage;

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();
        playerController = entityControllerProvider.get();
        playerController.playerState().bind(state);
        state.setValue(PlayerState.IDLE);
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
                    loadingPage.setDone();
                }));

        return loadingPage.parent();
    }

    private void drawMap() {
        // TODO keep controller in cache --> into init method keep playerController over lifetime of ingame
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

    private Parent drawPlayer(int posx, int posy) {
        tilePane.getChildren().remove(player);
        player = playerController.render();
        player.setTranslateX(posx * TILE_SIZE);
        player.setTranslateY(posy * TILE_SIZE);
        tilePane.getChildren().add(player);
        return player;
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
        return view;
    }

    private void movePlayer(int x, int y) {
        player = drawPlayer(x, y);
        player.toFront();
        player.setTranslateX(x * TILE_SIZE);
        player.setTranslateY(y * TILE_SIZE);
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

    @Override
    public void onResize(int width, int height) {
        this.width = width;
        this.height = height;
        if (loadingPage.isDone()) {
            updateOrigin();
        }
    }

    private void updateServerPos(Direction direction) {
        JsonObject data = new JsonObject();
        data.add("_id", new JsonPrimitive("646c84a0f148f6eb461bf654"));
        data.add("area", new JsonPrimitive("645e32c6866ace359554a7fa"));
        data.add("x", new JsonPrimitive(posx));
        data.add("y", new JsonPrimitive(posy));
        data.add("direction", new JsonPrimitive(direction.ordinal()));

        JsonObject message = new JsonObject();
        message.add("event", new JsonPrimitive("areas.645e32c6866ace359554a7fa.trainers.646c84a0f148f6eb461bf654.moved"));
        message.add("data", data);

        udpEventListener.send(message.toString());
    }

    @FXML
    public void handleKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            PauseController controller = pauseControllerProvider.get();
            controller.setRegion(region);
            app.show(controller);
        }

        if (keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.W)) {
            posy--;
            direction = Direction.UP;
        } else if (keyEvent.getCode().equals(KeyCode.DOWN) || keyEvent.getCode().equals(KeyCode.S)) {
            posy++;
            direction = Direction.DOWN;
        } else if (keyEvent.getCode().equals(KeyCode.LEFT) || keyEvent.getCode().equals(KeyCode.A)) {
            posx--;
            direction = Direction.LEFT;
        } else if (keyEvent.getCode().equals(KeyCode.RIGHT) || keyEvent.getCode().equals(KeyCode.D)) {
            posx++;
            direction = Direction.RIGHT;
        }
        state.setValue(PlayerState.WALKING);
        updateServerPos(direction);
    }

    @Override
    public String getTitle() {
        return resources.getString("titleIngame");
    }

    @Override
    public void destroy() {
        super.destroy();
        playerController.destroy();
    }

    @FXML
    public void setIdleState() {
        state.setValue(PlayerState.IDLE);
        drawPlayer(posx, posy);
    }
}
