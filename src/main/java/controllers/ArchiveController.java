package main.java.controllers;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import main.java.database.dao.PatientDao;
import main.java.database.entities.Patient;


public class ArchiveController {

    @FXML
    public TableView<Patient> patientTableView;

    @FXML
    public TableColumn<Patient, Double> idColumn;
    @FXML
    public TableColumn<Patient,String> nameColumn;
    @FXML
    public TableColumn<Patient,String> birthdateColumn;
    @FXML
    public TableColumn<Patient,String> recordDateColumn;
    @FXML
    public TableColumn<Patient,String> raceColumn;
    @FXML
    public TableColumn<Patient,String> genderColumn;

    @FXML
    public AnchorPane handleArchive;

    EntryController entryController = new EntryController();

    // TODO change empty table text to "No Patients Found" etc
    public void initialize() throws IOException {
        this.setCellValueFactories();
        this.setDoubleClickHandler();

        PatientDao patientDao = new PatientDao();
        List<Patient> patients = patientDao.getAll();
        ObservableList<Patient> patientObservableArray = FXCollections.observableArrayList(patients);

        patientTableView.setItems(patientObservableArray);
    }

    private void setCellValueFactories(){
        idColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, Double>("id")
        );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, String>("name")
        );
        birthdateColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, String>("birthDay")
        );
        recordDateColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, String>("registerDate")
        );
        raceColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, String>("race")
        );
        genderColumn.setCellValueFactory(
                new PropertyValueFactory<Patient, String>("gender")
        );
    }

    private void setDoubleClickHandler(){
        patientTableView.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Patient selectedPatient = row.getItem();
                    System.out.println(selectedPatient);
                    try {
                        this.callDrawingPanel(selectedPatient);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }

    private void callDrawingPanel(Patient selectedPatient) throws IOException {
        entryController.setActivePatient(selectedPatient);
        entryController.callDrawingPanel();
    }
}
