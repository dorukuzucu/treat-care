package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.utils.UserDataHandler;
import main.java.database.entities.Patient;
import main.java.utils.PaneEnum;

import java.io.IOException;


public class EntryController{
    @FXML
    public BorderPane mainPane = new BorderPane();
    static BorderPane mainPaneStatic;

    @FXML
    private void callNewPatient(ActionEvent event) throws IOException {
        mainPaneStatic = mainPane;
        Pane view = PaneLoader.getInstance().getPane(PaneEnum.NEW_PATIENT);
        mainPane.setCenter(view);
   }

    @FXML
    private void callArchive(ActionEvent event) throws IOException {
        mainPaneStatic = mainPane;
        Pane view = PaneLoader.getInstance().getPane(PaneEnum.ARCHIVE);
        mainPane.setCenter(view);
    }

    @FXML
    public void callDrawingPanel() throws IOException {
        mainPane = mainPaneStatic;
        Pane view = PaneLoader.getInstance().getPane(PaneEnum.DRAWING);
        mainPane.setCenter(view);
    }

    public void setActivePatient(Patient activePatient) {
        UserDataHandler.getInstance().setActivePatient(activePatient);
    }

}
