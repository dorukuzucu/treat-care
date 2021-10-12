package main.java.utils;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.analysis.results.IResult;
import main.java.analysis.utils.CalculationTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DisplayIResult {

    TableView<List<String>> tableView;
    Stage window;

    public void display(HashMap<CalculationTypeEnum, IResult> results, String title){
        this.tableView = new TableView<>();
        this.window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);
        window.setMinHeight(500);

        this.setData(results,title);

        VBox layout = new VBox(10);
        layout.getChildren().add(tableView);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private void setData(HashMap<CalculationTypeEnum, IResult> results, String title){
        List<List<String>> resultsString = this.resultToStringArray(results);

        TableColumn<List<String>, String> analysisName = new TableColumn<>("Calculation");
        analysisName.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(0)));

        TableColumn<List<String>, String> result = new TableColumn<>("Result");
        result.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().get(1)));

        tableView.getColumns().addAll(analysisName,result);
        ObservableList<List<String>> tableData = FXCollections.observableArrayList(resultsString);
        tableView.setItems(tableData);
    }

    private List<List<String>> resultToStringArray(HashMap<CalculationTypeEnum, IResult> results){
        List<List<String>> resultString = new ArrayList<>();

        for(Map.Entry<CalculationTypeEnum, IResult> resultEntry: results.entrySet()){
            List<String> item = new ArrayList<>();
            CalculationTypeEnum calculationType = resultEntry.getKey();
            IResult result = resultEntry.getValue();
            item.add(calculationType.toString());
            item.add(result.toString());
            resultString.add(item);
        }
        return resultString;
    }
}

