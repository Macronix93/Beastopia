package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.*;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.RegionService;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import de.uniks.beastopia.teaml.utils.LoadingPage;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.List;

public class MapController extends Controller {
    @FXML
    public Pane mapPane;
    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Button fastTravelButton;
    @Inject
    App app;
    @Inject
    DataCache cache;
    @Inject
    PresetsService presetsService;
    @Inject
    RegionService regionService;
    @Inject
    Provider<RegionInfoController> regionInfoControllerProvider;
    @Inject
    TrainerService trainerService;
    private LoadingPage loadingPage;
    private TileSet tileSet;
    private Image image;
    private Map map;
    private Area currentArea;
    private String fastTravelAreaName;

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
        this.map = cache.getJoinedRegion().map();
        this.tileSet = cache.getMapTileset();
        this.image = cache.getMapImage();
    }

    @Override
    public Parent render() {
        loadingPage = LoadingPage.makeLoadingPage(super.render());
        fastTravelButton.setVisible(false);
        drawMap();

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
        loadingPage.setDone();
    }

    private void drawObjectGroup(Layer layer) {
        for (MapObject object : layer.objects()) {
            RegionInfoController regionInfo = regionInfoControllerProvider.get();
            if (object.polygon() == null) {
                drawRectangle(object, regionInfo);
            } else {
                drawPolygon(object, regionInfo);
            }
        }
    }

    private void drawRectangle(MapObject object, RegionInfoController regionInfo) {
        Rectangle r = new Rectangle();
        r.setX(object.x());
        r.setY(object.y());
        r.setWidth(object.width());
        r.setHeight(object.height());
        //noinspection DuplicatedCode
        if (object.name().equals(currentArea.name())) {
            r.setStroke(Color.BLUEVIOLET);
            r.setStrokeWidth(3);
        } else if (cache.areaVisited(object.name())) {
            r.setStroke(Color.DARKGREEN);
            r.setStrokeWidth(3);
        }
        r.setFill(Color.TRANSPARENT);
        r.setOnMouseEntered(event -> setRegionInfo(r, object, regionInfo, event));
        r.setOnMouseExited(event -> anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1));
        anchorPane.getChildren().add(r);
    }

    private void drawPolygon(MapObject object, RegionInfoController regionInfo) {
        Polygon p = new Polygon();
        for (HashMap<String, Double> point : object.polygon()) {
            double x = point.get("x") + object.x();
            double y = point.get("y") + object.y();
            p.getPoints().addAll(x, y);
        }
        //noinspection DuplicatedCode
        if (object.name().equals(currentArea.name())) {
            p.setStroke(Color.BLUEVIOLET);
            p.setStrokeWidth(3);
        } else if (cache.areaVisited(object.name())) {
            p.setStroke(Color.DARKGREEN);
            p.setStrokeWidth(3);
        }
        p.setFill(Color.TRANSPARENT);
        p.setOnMouseEntered(event -> setRegionInfo(p, object, regionInfo, event));
        p.setOnMouseExited(event -> anchorPane.getChildren().remove(anchorPane.getChildren().size() - 1));
        anchorPane.getChildren().add(p);
    }

    private void setRegionInfo(Shape shape, MapObject object, RegionInfoController regionInfo, MouseEvent event) {
        String name = object.name();
        String description = object.properties().get(0).get("value");
        if (!cache.areaVisited(name)) {
            name = "Unknown area";
            description = "This area is unknown. New mysteries, adventures and dangers are awaiting you!";
        }
        shape.setOnMouseClicked(clickEvent -> setFastTravelTarget(cache.isFastTravelable(object.name()) ? object.name() : null));
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
        if (layer.chunks() != null) {
            for (Chunk chunk : layer.chunks()) {
                int chunkX = chunk.x();
                int chunkY = chunk.y();
                setTile(TILE_SIZE, chunk.data(), chunkX, chunkY, chunk.width());
            }
        } else if (layer.data() != null) {
            int chunkX = layer.x();
            int chunkY = layer.y();
            setTile(TILE_SIZE, layer.data(), chunkX, chunkY, layer.width());
        }
    }

    private void setTile(int TILE_SIZE, List<Long> data, int xPos, int yPos, int width) {
        int index = 0;
        for (long id : data) {
            int x = index % width + xPos;
            int y = index / width + yPos;
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

    private void setFastTravelTarget(String areaName) {
        if (!cache.areaVisited(areaName) || areaName.equals(currentArea.name())) {
            areaName = null;
        }

        fastTravelAreaName = areaName;
        fastTravelButton.setVisible(areaName != null);

        if (areaName != null) {
            fastTravelButton.setText(resources.getString("FastTravel") + " " + areaName);
        } else {
            fastTravelButton.setText(resources.getString("FastTravel"));
        }
    }

    @FXML
    private void fastTravel() {
        if (fastTravelAreaName == null) {
            return;
        }

        Trainer trainer = cache.getTrainer();
        Area newArea = cache.getAreaByName(fastTravelAreaName);
        disposables.add(trainerService.fastTravelTrainer(trainer.region(), trainer._id(), trainer.name(), trainer.image(), trainer.team(), newArea._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(newTrainer -> {
                    cache.setTrainer(newTrainer);
                    app.showPrevious();
                }, error -> Dialog.error(error, "Fast travel failed")));
    }

    @FXML
    private void closeMap() {
        app.showPrevious();
    }
}
