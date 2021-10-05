package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.database.DatabaseCreator;

import javax.swing.text.html.ImageView;
import java.awt.*;

public class Runner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       Class.forName("org.sqlite.JDBC");

        DatabaseCreator creator = new DatabaseCreator();
        creator.createAllTables();

        Parent root = FXMLLoader.load(getClass().getResource("/Entry.fxml"));
        primaryStage.setTitle("TreatCare Software Systems");
        Scene scene = new Scene(root,1200,950);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(950);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
