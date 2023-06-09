package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.MenuController;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.RegionService;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;

public class MapController extends Controller {
    @FXML
    public Pane mapPane;
    @FXML
    public AnchorPane anchorPane;
    @Inject
    App app;
    @Inject
    DataCache cache;
    @Inject
    PresetsService presetsService;
    @Inject
    RegionService regionService;
    @Inject
    Provider<IngameController> ingameControllerProvider;
    private Region region;
    private IngameController backController;
    private LoadingPage loadingPage;
    private TileSet tileSet;
    private Image image;
    private Map map;
    @Inject
    Provider<RegionInfoController> regionInfoControllerProvider;

    // TODO: remove this backpass
    @Inject
    Provider<MenuController> menuControllerProvider;

    @Inject
    public MapController() {
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());
        disposables.add(regionService.getRegion(cache.getJoinedRegion()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(region -> {
                            this.region = region;
                            this.map = region.map();
                            this.tileSet = presetsService.getTileset(map.tilesets().get(0)).blockingFirst();
                            this.image = presetsService.getImage(tileSet).blockingFirst();
                            loadingPage.setDone();
                            drawMap();
                        },
                        error -> System.out.println(error.getMessage())
                ));
        return loadingPage.parent();
    }

    private void drawMap() {
        for (Layer layer : this.map.layers()) {
            if (layer.type().equals("tilelayer")) {
                drawTileLayer(layer);
            } else if (layer.type().equals("objectgroup")) {
                drawObjectGroup(layer);
            }
        }
    }

    private void drawObjectGroup(Layer layer) {
        for (MapObject object : layer.objects()) {
            RegionInfoController regionInfo = regionInfoControllerProvider.get();
            regionInfo.init();
            if (object.polygon() == null) {
                Rectangle r = new Rectangle();
                r.setX(object.x());
                r.setY(object.y());
                r.setWidth(object.width());
                r.setHeight(object.height());
                r.setFill(javafx.scene.paint.Color.RED);
                r.setOnMouseEntered(event -> {
                    System.out.println("entered");
                    r.setFill(Color.GREEN);
                });
                r.setOnMouseExited(event -> {
                    System.out.println("exited");
                    r.setFill(Color.RED);
                });
                mapPane.getChildren().add(r);
            } else {
                Polygon p = new Polygon();
                for (HashMap<String, Double> point : object.polygon()) {
                    double x = point.get("x") + object.x();
                    double y = point.get("y") + object.y();
                    p.getPoints().addAll(x, y);
                }
                p.fillProperty().setValue(Color.BLUEVIOLET);
                p.setOnMouseEntered(event -> {
                    String name = object.name();
                    String description = object.properties().get(0).get("value");
                    regionInfo.setText(name, description);
                    System.out.println("entered");
                    p.setFill(Color.GREEN);
                    anchorPane.getChildren().add(regionInfo.render());
                    anchorPane.getChildren().get(1).setLayoutX(event.getX() + 10);
                    anchorPane.getChildren().get(1).setLayoutY(event.getY() + 10);
                });
                p.setOnMouseExited(event -> {
                    System.out.println("exited");
                    p.setFill(Color.BLUEVIOLET);
                    anchorPane.getChildren().remove(1);
                });
                mapPane.getChildren().add(p);
            }

        }
    }

    private void drawTileLayer(Layer layer) {
        int TILE_SIZE = 16;
        for (Chunk chunk : layer.chunks()) {
            int chunkX = chunk.x();
            int chunkY = chunk.y();
            int index = 0;
            for (int id : chunk.data()) {
                int x = index % chunk.width() + chunkX;
                int y = index / chunk.height() + chunkY;
                index++;
                ImageView view = new ImageView();
                view.setPreserveRatio(true);
                view.setSmooth(true);
                view.setImage(image);
                view.setFitWidth(TILE_SIZE + 1);
                view.setFitHeight(TILE_SIZE + 1);
                view.setViewport(presetsService.getTileViewPort(id, tileSet));
                view.setTranslateX(x * TILE_SIZE);
                view.setTranslateY(y * TILE_SIZE);
                mapPane.getChildren().add(view);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public void closeMap() {
        IngameController ingameController = ingameControllerProvider.get();
        ingameController.setRegion(region);

        // TODO: remove this backpass
        MenuController menuController = menuControllerProvider.get();

        app.show(menuController);
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setBackController(IngameController ingameController) {
        this.backController = ingameController;
    }

    public void giveCor(MouseEvent mouseEvent) {
        //System.out.println("x: " + mouseEvent.getX() + " y: " + mouseEvent.getY());
    }
}
