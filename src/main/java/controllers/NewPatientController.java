package main.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.java.database.entities.Patient;
import main.java.utils.GetCurrentTime;
import main.java.utils.SimpleAlertWindow;
import main.java.utils.UserDataHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class NewPatientController {
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public ComboBox<String> genderComboBox;
    @FXML
    public ComboBox<String> raceComboBox;
    @FXML
    public DatePicker birthdayDatePicker;
    @FXML
    public Button nextButton;

    EntryController entryController = new EntryController();

    public void initialize(){
        this.genderComboBox.getItems().addAll("Male","Female");
        this.raceComboBox.getItems().addAll("Beyaz Irk","Afrikalı","Asyalı");
    }

    private Boolean inputDataIsValid(){
        if(firstNameField.getText().trim().isEmpty())
            return false;
        if(lastNameField.getText().trim().isEmpty())
            return false;
        if(birthdayDatePicker.getValue()==null)
            return false;
        if(genderComboBox.getValue()==null)
            return false;
        if(raceComboBox.getValue()==null)
            return false;
        return true;
    }

    private void showAlertWindow(String title, String message){
        SimpleAlertWindow.display(title, message);
    }

    @FXML
    private void nextButtonClicked(ActionEvent event) throws IOException
    {
        if(this.inputDataIsValid()) {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String fullName = firstName+" "+lastName;
            LocalDate birthDay = birthdayDatePicker.getValue();
            String gender = genderComboBox.getValue();
            String race = raceComboBox.getValue();

            Patient newPatient = new Patient((double) 0, fullName, birthDay.toString(), GetCurrentTime.now(), gender, race);
            UserDataHandler.getInstance().setActivePatient(newPatient);
            try {
                UserDataHandler.getInstance().saveActivePatient();
                entryController.callDrawingPanel();
            } catch (SQLException e) {
                UserDataHandler.getInstance().clearData();
                this.showAlertWindow("Error", "User can not be saved");
            }
        } else {
            this.showAlertWindow("Warning", "Please Fill in All Required Fields");
        }
    }




}
