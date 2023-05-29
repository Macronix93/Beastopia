package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.App;
import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.controller.menu.PauseController;
import de.uniks.beastopia.teaml.service.PresetsService;
import de.uniks.beastopia.teaml.service.TokenStorage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javax.inject.Inject;
import javax.inject.Provider;

public class IngameController extends Controller {
    @FXML
    public HBox ingame;
    @FXML
    public Button pauseButton;
    @Inject
    App app;
    @Inject
    Provider<PauseController> pauseControllerProvider;

    @Inject
    TokenStorage tokenStorage;
    @Inject
    PresetsService presetsService;
    @FXML
    public ImageView showSprite;

    @Inject
    public IngameController() {
    }

    @Override
    public void init() {
        super.init();

        disposables.add(presetsService.getCharacterSprites(tokenStorage.getCurrentTrainer().image())
                .observeOn(FX_SCHEDULER)
                .subscribe(res -> {
                    showSprite.setImage(res);
                    showSprite.setPreserveRatio(true);
                    showSprite.setSmooth(true);
                    showSprite.setViewport(new javafx.geometry.Rectangle2D(48, 0, 16, 32));
                }));
    }

    @FXML
    public void pauseMenu(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
            app.show(pauseControllerProvider.get());
        }
    }

    @Override
    public String getTitle() {
        return resources.getString("titleIngame");
    }
}
