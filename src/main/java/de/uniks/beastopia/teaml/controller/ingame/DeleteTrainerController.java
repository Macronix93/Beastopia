package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import de.uniks.beastopia.teaml.rest.Region;
import de.uniks.beastopia.teaml.service.DataCache;
import de.uniks.beastopia.teaml.service.TrainerService;
import de.uniks.beastopia.teaml.utils.Dialog;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import javax.inject.Provider;

public class DeleteTrainerController extends Controller {
    @FXML
    public TextField trainerNameField;
    @FXML
    public ImageView trainerSprite;

    @Inject
    DataCache cache;
    @Inject
    TrainerService trainerService;
    @Inject
    Provider<TrainerController> trainerControllerProvider;

    private Region region;


    @Inject
    public DeleteTrainerController() {
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        trainerSprite.setImage(cache.getCharacterImage(cache.getTrainer().image()).getValue());
        trainerSprite.setPreserveRatio(true);
        trainerSprite.setViewport(TrainerController.PREVIEW_VIEWPORT);
        trainerSprite.setFitWidth(32 * TrainerController.PREVIEW_SCALING);
        trainerSprite.setFitHeight(32 * TrainerController.PREVIEW_SCALING);
        trainerSprite.setSmooth(false);
        return parent;
    }

    @Override
    public String getTitle() {
        return resources.getString("titleTrainerDeletion");
    }

    public void deleteTrainer() {
        if (trainerNameField == null || trainerNameField.getText().isEmpty() || !trainerNameField.getText().equals(cache.getTrainer().name())) {
            Dialog.error(resources.getString("trainerNameMissing"), resources.getString("trainerDeletionNameMissing"));
            return;
        }

        disposables.add(trainerService.deleteTrainer(region._id(), cache.getTrainer()._id())
                .observeOn(FX_SCHEDULER)
                .subscribe(tr -> {
                    if (tr != null) {
                        cache.setTrainer(null);

                        TrainerController trainerController = trainerControllerProvider.get();
                        trainerController.setRegion(region);
                        trainerController.backController("menu");
                        app.show(trainerController);
                    }
                }));
    }

    public void cancel() {
        TrainerController trainerController = trainerControllerProvider.get();
        trainerController.setRegion(region);
        trainerController.backController("pause");
        app.show(trainerController);
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
