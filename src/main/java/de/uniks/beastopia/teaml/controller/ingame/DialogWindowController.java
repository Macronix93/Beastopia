package de.uniks.beastopia.teaml.controller.ingame;

import de.uniks.beastopia.teaml.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.text.Text;

public class DialogWindowController extends Controller {

    @FXML
    Text dialogWindow;

    @Override
    public String getTitle() {
        return resources.getString("titleDialogWindow");
    }

    @Override
    public Parent render() {
        Parent parent = super.render();

        return parent;
    }
}
