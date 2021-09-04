package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.database.DatabaseCreator;

public class Runner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Class.forName("org.sqlite.JDBC");

        DatabaseCreator creator = new DatabaseCreator();
        creator.createAllTables();

        Parent root = FXMLLoader.load(getClass().getResource("../resources/Entry.fxml"));
        primaryStage.setTitle("TreatCare Software Systems");

        Scene scene = new Scene(root,1200,950);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(950);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest( e -> SystemClose());
        primaryStage.show();
    }

    private void SystemClose(){
       // System.exit(0);           BURASI AÇIKKEN NEDENSE KASIYOR??
    }

    public static void main(String[] args){
        launch(args);
    }
}
