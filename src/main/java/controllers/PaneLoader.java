package main.java.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import main.java.utils.PaneEnum;


import java.io.IOException;
import java.util.HashMap;

public class PaneLoader {

    private Pane view;
    private final HashMap<PaneEnum, String> paneHashMap = new HashMap<>();
    private static PaneLoader instance = null;

    private PaneLoader() throws IOException {
        this.initializePanes();
    }

    private void initializePanes() throws IOException {
        this.addPane(PaneEnum.ARCHIVE, "/Archive.fxml");
        this.addPane(PaneEnum.DRAWING, "/DrawingPanel.fxml");
        //TODO: Parent loader, this.addPane(PaneEnum.ENHANCE_GUI, readFxmlToPane("/EnhanceGUI.fxml"));
        this.addPane(PaneEnum.ENTRY, "/Entry.fxml");
        this.addPane(PaneEnum.NEW_PATIENT, "/NewPatient.fxml");
        //TODO: Parent loader, this.addPane(PaneEnum.WRONG_EXTENSION, readFxmlToPane("/WrongExtensionAlert.fxml"));

        System.out.println(paneHashMap) ;
    }

    public void addPane(PaneEnum name, String panePath){
        paneHashMap.put(name, panePath);
    }

    private Pane readFxmlToPane(String fxmlPath) throws IOException {
        Pane pane = FXMLLoader.load(getClass().getResource(fxmlPath));
        return pane;
    }

    public Pane getPane(PaneEnum paneName) throws IOException {
        String panePath = paneHashMap.get(paneName);
        return readFxmlToPane(panePath);
    }

    public static PaneLoader getInstance() throws IOException {
        if (instance==null){
            instance = new PaneLoader();
        }
        return instance;
    }


}
