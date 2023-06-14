package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
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
import javafx.scene.shape.Circle;
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
    @Inject
    Provider<RegionInfoController> regionInfoControllerProvider;
    private Region region;
    private LoadingPage loadingPage;
    private TileSet tileSet;
    private Image image;
    private Map map;
    private Area currentArea;

    @Inject
    public MapController() {
    }

    @Override
    public String getTitle() {
        return resources.getString("TitleMap");
    }

    @Override
    public void init() {
        super.init();
        Trainer trainerData = cache.getTrainer();
        currentArea = cache.getArea(trainerData.area());
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
                        error -> {
                            throw new RuntimeException(error);
                        }
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
            if (object.name().equals(currentArea.name())) {
                markOwnPlayer(object);
            }
            if (object.polygon() == null) {
                Rectangle r = new Rectangle();
                r.setX(object.x());
                r.setY(object.y());
                r.setWidth(object.width());
                r.setHeight(object.height());
                r.setFill(Color.TRANSPARENT);
                r.setOnMouseEntered(event -> setRegionInfo(object, regionInfo, event));
                r.setOnMouseExited(event -> anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1));
                anchorPane.getChildren().add(r);
            } else {
                Polygon p = new Polygon();
                for (HashMap<String, Double> point : object.polygon()) {
                    double x = point.get("x") + object.x();
                    double y = point.get("y") + object.y();
                    p.getPoints().addAll(x, y);
                }
                p.setFill(Color.TRANSPARENT);
                p.setOnMouseEntered(event -> setRegionInfo(object, regionInfo, event));
                p.setOnMouseExited(event -> anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1));
                anchorPane.getChildren().add(p);
            }

        }
    }

    private void markOwnPlayer(MapObject object) {
        Circle c = new Circle();
        c.setRadius(5);
        c.setCenterX(object.x());
        c.setCenterY(object.y());
        c.setFill(Color.BLUEVIOLET);
        anchorPane.getChildren().add(c);
    }

    private void setRegionInfo(MapObject object, RegionInfoController regionInfo, MouseEvent event) {
        String name = object.name();
        String description = object.properties().get(0).get("value");
        regionInfo.setText(name, description);
        double maxX = anchorPane.widthProperty().getValue();
        double maxY = anchorPane.heightProperty().getValue();
        anchorPane.getChildren().add(regionInfo.render());
        int lastChild = anchorPane.getChildren().size() - 1;

        if (event.getX() + 10 + 231 > maxX) {
            anchorPane.getChildren().get(lastChild).setLayoutX(event.getX() - 231 - 10);
        } else {
            anchorPane.getChildren().get(lastChild).setLayoutX(event.getX() + 10);
        }

        if (event.getY() + 10 + 131 > maxY) {
            anchorPane.getChildren().get(lastChild).setLayoutY(event.getY() - 131 - 10);
        } else {
            anchorPane.getChildren().get(lastChild).setLayoutY(event.getY() + 10);
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
                anchorPane.getChildren().add(view);
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
        app.show(ingameController);
    }
}