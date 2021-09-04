package main.java.controllers;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.java.utils.PaneEnum;


public class SceneController {
    private HashMap<PaneEnum, Scene> sceneMap = new HashMap<>();
    private Stage stage;
    private static SceneController instance = null;

    private SceneController() throws IOException {
        this.initializeScenes();
    }

    private void initializeScenes() throws IOException {
        this.addScene(PaneEnum.ARCHIVE,readFxmlToScene("/Archive.fxml"));
        this.addScene(PaneEnum.DRAWING,readFxmlToScene("/DrawingPanel.fxml"));
        this.addScene(PaneEnum.ENHANCE_GUI,readFxmlToScene("/EnhanceGUI.fxml"));
        this.addScene(PaneEnum.ENTRY,readFxmlToScene("/Entry.fxml"));
        this.addScene(PaneEnum.NEW_PATIENT,readFxmlToScene("/NewPatient.fxml"));
        this.addScene(PaneEnum.WRONG_EXTENSION,readFxmlToScene("/WrongExtensionAlert.fxml"));

    }

    private Scene readFxmlToScene(String fxmlPath) throws IOException {
        Parent resource = FXMLLoader.load(getClass().getResource(fxmlPath));
        return new Scene(resource);
    }

    public void setMainStage(Stage mainStage){
        stage = mainStage;
    }
    public void addScene(PaneEnum name, Scene scene){
        sceneMap.put(name, scene);
    }

    public void removeScene(PaneEnum name){
        sceneMap.remove(name);
    }
    public void getScene(PaneEnum name){
        sceneMap.get(name);
    }

    public void activate(PaneEnum name){
        Scene scene = sceneMap.get(name);
        stage.setScene(scene);
    }

    public static SceneController getInstance() throws IOException {
        if (instance==null){
            instance = new SceneController();
        }
        return instance;
    }

    public Stage getStage() {
        return stage;
    }
}
