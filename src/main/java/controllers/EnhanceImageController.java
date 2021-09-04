package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;


public class EnhanceImageController {


    DrawingController con = new DrawingController();

    @FXML
    private Slider brightnessSlider;
    @FXML
    private Slider contrastSlider;

    public static float brightnessValue;
    public static float contrastValue;

    @FXML
    public Button reset;

    @FXML
    public void onBrightnessSliderChanged() {
        brightnessValue = (float) brightnessSlider.getValue();  //Double to float
        String.format("%.1f" ,brightnessValue );
        con.brightnessAdjustments(brightnessValue);
    }

    @FXML
    public void onContrastSliderChange() {
        contrastValue = (float) contrastSlider.getValue();
        String.format("%.1f" ,contrastValue );
        con.contrastAdjustments(contrastValue);
    }

    @FXML
    public void resetImage() {
        contrastValue = 0;
        brightnessValue = 0;
        brightnessSlider.setValue(0);
        contrastSlider.setValue(0);
        con.brightnessAdjustments(brightnessValue);
        con.contrastAdjustments(contrastValue);
    }
}
