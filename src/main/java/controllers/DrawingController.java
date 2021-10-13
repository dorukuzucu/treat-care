package main.java.controllers;


import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import main.java.analysis.results.IResult;
import main.java.analysis.sets.AnalysisFactory;
import main.java.analysis.sets.IAnalysisSet;
import main.java.analysis.utils.AnalysisEnum;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.database.dao.PatientImageDao;
import main.java.database.entities.Patient;
import main.java.database.entities.PatientImage;
import main.java.drawing.MainPaneCircleDrawer;
import main.java.utils.GetCurrentTime;
import main.java.utils.PointEnum;
import main.java.utils.DisplayIResult;
import main.java.utils.UserDataHandler;

import javax.imageio.ImageIO;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.List;

public class DrawingController {

    @FXML
    public AnchorPane mainFrame;

    @FXML
    public AnchorPane zoomAnchor;

    @FXML
    public AnchorPane cephImageAnchor;

    @FXML
    public Label nameLastNameLabel;
    @FXML
    public Label birthDateLabel;
    @FXML
    public Label currentAgeLabel;
    @FXML
    public Label genderLabel;
    @FXML
    public Label raceLabel;


    @FXML
    public Button saveButton;

    @FXML
    public Button clearButton;

    @FXML
    public Button exportButton;


    @FXML
    public Label currentDateLabel;

    @FXML
    ImageView cephImageView;

    @FXML
    ImageView zoomedImage;


    @FXML
    public ScrollPane scrollCephImage;

    static ImageView alteredCephImage;

    static ImageView alteredZoomImage;

    @FXML
    public Button enhance;
    @FXML
    public Button hideLabel;

    @FXML
    public ComboBox<AnalysisEnum> analyseComboBox;

    @FXML
    private Slider rulerSlider;
    public static double rulerSliderValue;

    @FXML
    private Label rulerLabel;
    @FXML
    private Slider ZoomSlider;
    private double zoomValue = 200;
    private double zoomSliderValue;

    @FXML
    private Slider rotationSlider;
    @FXML
    public ListView<PointEnum> anatomicalPoints;

    @FXML
    private Line vLine;
    @FXML
    private Line hLine;


    private ArrayList<Circle> displayedCircle;
    ArrayList<PointEnum> pointList = new ArrayList<>();

    private UserDataHandler userDataHandler = UserDataHandler.getInstance();
    private MainPaneCircleDrawer mainPaneCircleDrawer;

    ColorAdjust colorAdjust = new ColorAdjust();


    @FXML
    public void exportImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(mainFrame.getScene().getWindow());
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(cephImageAnchor.snapshot(null, null), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearAll() {
        this.mainPaneCircleDrawer.removeAllPoints();
        mainFrame.getChildren().removeIf(Path.class::isInstance);
        anatomicalPoints.getSelectionModel().select(0);
    }

    public void hideAllPointsAndLabels() {
        this.mainPaneCircleDrawer.hideAllPoints();
        this.mainPaneCircleDrawer.hideAllLabels();
    }

    public void initialize() {
        hLine.setVisible(false);
        vLine.setVisible(false);
        this.mainPaneCircleDrawer = new MainPaneCircleDrawer(this.mainFrame,
                //(this.anatomicalPoints.getLayoutX() + this.anatomicalPoints.getWidth()),
                390,
                this.anatomicalPoints.getLayoutY());

        rulerSliderValue = rulerSlider.getValue();
        this.setPatientInfoLabels();
        this.displayPointList();

        this.displayAnalysis();

        cephImageView.setOnMouseEntered(e -> {
            if (cephImageView.getImage() != null) {
                hLine.setVisible(true);
                vLine.setVisible(true);

                zoomedImage.setVisible(true);
                zoomSliderValue = ZoomSlider.getValue();
            }
        });

        cephImageView.setOnMouseMoved(e -> {
            if (cephImageView.getImage() != null) {

                Image image = cephImageView.getImage();

                double x = e.getX();
                double y = e.getY();


                double xScale = (cephImageView.getFitWidth() > 0 ?
                        image.getWidth() / cephImageView.getFitWidth() : 1);
                double yScale = (cephImageView.getFitHeight() > 0 ?
                        image.getHeight() / cephImageView.getFitHeight() : 1);
                if (cephImageView.isPreserveRatio()) {
                    double scale = Math.max(xScale, yScale);
                    x *= scale;
                    y *= scale;
                } else {
                    x *= xScale;
                    y *= yScale;
                }

                x = Math.max(0, x - zoomValue / zoomSliderValue); // Default was 125
                y = Math.max(0, y - zoomValue / zoomSliderValue);// Default was 125
                double width = Math.min(image.getWidth() - x, 2 * zoomValue / zoomSliderValue);  // Default was 250
                double height = Math.min(image.getHeight() - y, 2 * zoomValue / zoomSliderValue);// Default was 250

                PixelReader reader = image.getPixelReader();
                WritableImage newImage = new WritableImage(reader, (int) x, (int) y, (int) width, (int) height);

                alteredZoomImage = zoomedImage;
                zoomedImage.setImage(newImage);
            }
        });

        cephImageView.setOnMouseExited(e -> {

            if (cephImageView.getImage() != null) {
                hLine.setVisible(false);
                vLine.setVisible(false);
                zoomedImage.setVisible(false);


            }
        });

        cephImageView.setOnMouseClicked(mouseEvent -> {
            if (cephImageView.getImage() != null) {
                int listIndex = this.anatomicalPoints.getSelectionModel().getSelectedIndex();
                PointEnum selectedPoint = this.anatomicalPoints.getSelectionModel().getSelectedItem();

                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if (listIndex <= pointList.size() - 2){
                        this.mainPaneCircleDrawer.drawPointAt(selectedPoint, mouseEvent.getX(), mouseEvent.getY());
                        this.anatomicalPoints.getSelectionModel().selectIndices(listIndex + 1);
                    }
                }
                else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                    if (listIndex > 0) {
                        this.mainPaneCircleDrawer.removePoint(selectedPoint);
                        this.anatomicalPoints.getSelectionModel().selectIndices(listIndex - 1);
                    }
                }
            }
        });

        userDataHandler.getPatientData();
        PatientImage imageData = userDataHandler.getActivePatientImage();
        if (!(imageData == null)) {
            this.setImageToImageView(imageData.getImage());
            this.mainPaneCircleDrawer.displayUserDataPoints();
        }
    }

    private void displayPointList() {
        pointList.addAll(Arrays.asList(PointEnum.values()));

        anatomicalPoints.getItems().addAll(pointList);
        anatomicalPoints.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        anatomicalPoints.getSelectionModel().select(0);
    }

    private void displayAnalysis() {
        List<AnalysisEnum> analysis = Arrays.asList(AnalysisEnum.values());
        analyseComboBox.getItems().addAll(analysis);
    }

    private void setPatientInfoLabels() {
        Patient activePatient = userDataHandler.getActivePatient();
        this.nameLastNameLabel.setText(activePatient.getName());
        this.genderLabel.setText(activePatient.getGender());
        this.raceLabel.setText(activePatient.getRace());
        this.currentDateLabel.setText(GetCurrentTime.now());

        LocalDate patientBirthDate = LocalDate.parse(activePatient.getBirthDay());
        this.birthDateLabel.setText(patientBirthDate.getDayOfMonth() + "." + patientBirthDate.getMonthValue() + "." + patientBirthDate.getYear());

        Period period = Period.between(patientBirthDate, LocalDate.now());
        if (period.getMonths() > 0) {
            this.currentAgeLabel.setText(period.getYears() + " Years " + period.getMonths() + " Months");
        } else {
            this.currentAgeLabel.setText(period.getYears() + " Years old");
        }
    }

    @FXML
    public void BrowseImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "PNG Files", "JFIF Files", "*.jpg", "*.png", "*.jfif"));
        File file = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
        if (file != null) {
            if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jfif")) {
                clearAll();
                this.activateSearchedImage(file);

            } else {
                Stage errorStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("resources/WrongExtensionAlert.fxml"));
                errorStage.setTitle("Error!");
                errorStage.setResizable(false);
                Scene scene = new Scene(root);
                errorStage.setScene(scene);
                errorStage.show();
            }
        }
    }

    private void activateSearchedImage(File imageFile) throws FileNotFoundException {

        InputStream fileInputStream = new FileInputStream(imageFile);
        this.setImageToImageView(fileInputStream);

        PatientImageDao patientImageDao = new PatientImageDao();
        PatientImage savedPatientImage = patientImageDao.newImage(userDataHandler.getActivePatient().getId(), imageFile);
        userDataHandler.setActivePatientImage(savedPatientImage);
    }

    private void setImageToImageView(InputStream imageFileInputStream) {
        Image image = new Image(imageFileInputStream);
        cephImageView.setImage(image);
        resetAdjustmentsForNewImage();
        alteredCephImage = cephImageView;
    }

    public void brightnessAdjustments(float brightnessValue) {
        colorAdjust.setBrightness(brightnessValue);
        alteredCephImage.setEffect(colorAdjust);
        cephImageView = alteredCephImage;
        alteredZoomImage.setEffect(colorAdjust);

    }

    public void contrastAdjustments(float contrastValue) {
        colorAdjust.setContrast(contrastValue);
        alteredCephImage.setEffect(colorAdjust);
        cephImageView = alteredCephImage;
        alteredZoomImage.setEffect(colorAdjust);
    }

    public void resetAdjustmentsForNewImage() {
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        EnhanceImageController.brightnessValue = 0;
        EnhanceImageController.contrastValue = 0;
        cephImageView.setEffect(colorAdjust);
        if (alteredZoomImage != null) {     // if not null , gives error
            alteredZoomImage.setEffect(colorAdjust);
        }
    }

    @FXML
    void EnhanceImagePopUp() throws IOException {
        if (cephImageView.getImage() != null) {
            Stage enhanceStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/EnhanceGUI.fxml"));
            enhanceStage.setTitle("EnhanceImageController");
            enhanceStage.setWidth(550);
            enhanceStage.setHeight(250);
            enhanceStage.setResizable(false);
            enhanceStage.setOnCloseRequest(e -> closeEnhanceGUI());
            enhanceStage.setScene(new Scene(root));
            enhanceStage.show();

            if (enhanceStage.isShowing()) {
                enhance.setDisable(true);
            }
        }
    }

    private void closeEnhanceGUI() {
        enhance.setDisable(false);
    }

    @FXML
    private void ChanceRulerSlider() {
        int i = (int) this.rulerSlider.getValue();
        rulerLabel.setText(i + " mm");
        rulerSliderValue = i;

    }



    @FXML
    private void ChanceZoomSlider() {
        float i = (float) ZoomSlider.getValue();
        //zoomLabel.setText("x" + (String.format("%.1f" ,i )));
    }

    //TODO move
    //  calculation to math utils
    //  drawing to MainPaneLineDrawer
    private Path drawLineBetweenPointsExactDistance(PointEnum first, PointEnum second) {
        Path path = new Path();

        double x1;
        double y1;
        double x2;
        double y2;

        x1 = this.mainPaneCircleDrawer.getPoint(first).getCenterX();
        y1 = this.mainPaneCircleDrawer.getPoint(first).getCenterY();

        x2 = this.mainPaneCircleDrawer.getPoint(second).getCenterX();
        y2 = this.mainPaneCircleDrawer.getPoint(second).getCenterY();

        path.getElements().add(new MoveTo(x1, y1));
        path.getElements().add(new LineTo(x2, y2));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;
    }

    //TODO move
    //  calculation to math utils
    //  drawing to MainPaneLineDrawer
    private Path drawLineBetweenPointsExtended(PointEnum first, PointEnum second, int lineDistance) {
        Path path = new Path();

        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;


        x1 = this.mainPaneCircleDrawer.getPoint(first).getCenterX();
        y1 = this.mainPaneCircleDrawer.getPoint(first).getCenterY();

        x2 = this.mainPaneCircleDrawer.getPoint(second).getCenterX();
        y2 = this.mainPaneCircleDrawer.getPoint(second).getCenterY();

        double d = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        double r = lineDistance / d;

        x3 = r * x2 + (1 - r) * x1;  //Some Cool Calculations
        y3 = r * y2 + (1 - r) * y1;

        path.getElements().add(new MoveTo(
                this.mainPaneCircleDrawer.getPoint(first).getCenterX(),
                this.mainPaneCircleDrawer.getPoint(first).getCenterY()
        ));
        path.getElements().add(new LineTo(x3, y3));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;


    }

    //TODO move
    //  calculation to math utils
    //  drawing to MainPaneLineDrawer
    private Path drawLinesUpToIntersection(PointEnum first, PointEnum second, PointEnum third, PointEnum fourth) {
        Path path = new Path();

        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double x4;
        double y4;

        x1 = this.mainPaneCircleDrawer.getPoint(first).getCenterX();
        y1 = this.mainPaneCircleDrawer.getPoint(first).getCenterY();

        x2 = this.mainPaneCircleDrawer.getPoint(second).getCenterX();
        y2 = this.mainPaneCircleDrawer.getPoint(second).getCenterY();

        x3 = this.mainPaneCircleDrawer.getPoint(third).getCenterX();
        y3 = this.mainPaneCircleDrawer.getPoint(third).getCenterY();

        x4 = this.mainPaneCircleDrawer.getPoint(fourth).getCenterX();
        y4 = this.mainPaneCircleDrawer.getPoint(fourth).getCenterY();

        double d1 = (x1 - x2) * (y3 - y4);
        double d2 = (y1 - y2) * (x3 - x4);
        double d = (d1) - (d2);

        double u1 = (x1 * y2 - y1 * x2);
        double u4 = (x3 * y4 - y3 * x4);

        double u2x = (x3 - x4);
        double u3x = (x1 - x2);
        double u2y = (y3 - y4);
        double u3y = (y1 - y2);

        double xIntercept = (u1 * u2x - u3x * u4) / d;
        double yIntercept = (u1 * u2y - u3y * u4) / d;

        path.getElements().add(new MoveTo(
                this.mainPaneCircleDrawer.getPoint(first).getCenterX(),
                this.mainPaneCircleDrawer.getPoint(first).getCenterY()
        ));
        path.getElements().add(new LineTo(xIntercept, yIntercept));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;
    }

    //TODO move
    //  calculation to math utils
    //  drawing to MainPaneLineDrawer
    private Path drawPerpendicularLine(PointEnum first, int lineDistance) {
        Path path = new Path();

        double x1;
        double y1;

        x1 = this.mainPaneCircleDrawer.getPoint(first).getCenterX();
        y1 = this.mainPaneCircleDrawer.getPoint(first).getCenterY();

        path.getElements().add(new MoveTo(
                this.mainPaneCircleDrawer.getPoint(first).getCenterX(),
                this.mainPaneCircleDrawer.getPoint(first).getCenterY()
        ));
        path.getElements().add(new LineTo(x1, y1 + lineDistance));   // same x value , different y value , defines perpendicular line
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;
    }

    //TODO move
    //  calculation to math utils
    //  drawing to MainPaneLineDrawer
    private Path drawObliqueLine(PointEnum first, PointEnum second, PointEnum third, PointEnum fourth, PointEnum fifth) {

        Path path = new Path();
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double x4;
        double y4;

        x1 = this.mainPaneCircleDrawer.getPoint(first).getCenterX();
        y1 = this.mainPaneCircleDrawer.getPoint(first).getCenterY();

        x2 = this.mainPaneCircleDrawer.getPoint(second).getCenterX();
        y2 = this.mainPaneCircleDrawer.getPoint(second).getCenterY();

        x3 = this.mainPaneCircleDrawer.getPoint(third).getCenterX();
        y3 = this.mainPaneCircleDrawer.getPoint(third).getCenterY();

        x4 = this.mainPaneCircleDrawer.getPoint(fourth).getCenterX();
        y4 = this.mainPaneCircleDrawer.getPoint(fourth).getCenterY();

        double d1 = (x1 - x2) * (y3 - y4);
        double d2 = (y1 - y2) * (x3 - x4);
        double d = (d1) - (d2);

        double u1 = (x1 * y2 - y1 * x2);
        double u4 = (x3 * y4 - y3 * x4);

        double u2x = (x3 - x4);
        double u3x = (x1 - x2);
        double u2y = (y3 - y4);
        double u3y = (y1 - y2);

        double xIntercept = (u1 * u2x - u3x * u4) / d;
        double yIntercept = (u1 * u2y - u3y * u4) / d;

        path.getElements().add(new MoveTo(
                this.mainPaneCircleDrawer.getPoint(fifth).getCenterX(),
                this.mainPaneCircleDrawer.getPoint(fifth).getCenterY()
        ));
        path.getElements().add(new LineTo(xIntercept, yIntercept));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;

    }

    //TODO split saving and calculation methods
    public void saveButtonClicked() {
        userDataHandler.saveImagePoints();
        AnalysisEnum selectedAnalysis = this.analyseComboBox.getValue();
        if (selectedAnalysis == null)
            return;

        if (selectedAnalysis.toString() == "STEINER") {


            mainFrame.getChildren().removeIf(Path.class::isInstance);  // avoid multiple path creating
            mainFrame.getChildren().addAll(
                    drawLineBetweenPointsExtended(PointEnum.MENTON, PointEnum.GONION, 300),
                    drawLineBetweenPointsExtended(PointEnum.OCCLUSAL_PLANE_INCISOR_EDGE, PointEnum.LOWER_MOLAR_MESIAL_TIP, 300),
                    drawLineBetweenPointsExtended(PointEnum.ORBITALE, PointEnum.PORION , 350),
                    drawLineBetweenPointsExtended(PointEnum.NASION, PointEnum.SELLA , 350)
                    );

            mainFrame.getChildren().addAll(
                    drawLinesUpToIntersection(PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.UPPER_INCISOR_ROOT_TIP, PointEnum.SELLA, PointEnum.NASION),
                    drawLinesUpToIntersection(PointEnum.LOWER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_ROOT_TIP, PointEnum.MENTON, PointEnum.GONION),
                    drawLinesUpToIntersection(PointEnum.ANS, PointEnum.PNS, PointEnum.MENTON, PointEnum.GONION),
                    drawLinesUpToIntersection(PointEnum.MENTON, PointEnum.GONION, PointEnum.ANS, PointEnum.PNS),
                    drawLinesUpToIntersection(PointEnum.SELLA, PointEnum.GNATHION, PointEnum.GONION, PointEnum.MENTON),
                    drawLinesUpToIntersection(PointEnum.GONION, PointEnum.MENTON, PointEnum.SELLA, PointEnum.GNATHION)
            );
            mainFrame.getChildren().addAll(
                    drawLineBetweenPointsExactDistance(PointEnum.MIDPOINT_OF_COLUMELLA, PointEnum.ST_POGONION),
                    drawLineBetweenPointsExactDistance(PointEnum.SELLA, PointEnum.ARTICULARE),
                    drawLineBetweenPointsExactDistance(PointEnum.SELLA, PointEnum.GNATHION),
                    drawLineBetweenPointsExactDistance(PointEnum.NASION, PointEnum.SELLA),
                    drawLineBetweenPointsExactDistance(PointEnum.ARTICULARE, PointEnum.GONION)
            );
        }
        if (selectedAnalysis.toString() == "RICKETS") {
            mainFrame.getChildren().removeIf(Path.class::isInstance);  // to avoid multiple path creating

            mainFrame.getChildren().addAll(
                    drawLineBetweenPointsExactDistance(PointEnum.TIP_OF_NOSE, PointEnum.ST_POGONION),  //34 - TIP_OF_NOSE , 43 - ST_pogonion
                    drawLineBetweenPointsExtended(PointEnum.NASION, PointEnum.BASION, 450), // 5 - Basion, 2- Nasion
                    drawLineBetweenPointsExtended(PointEnum.OCCLUSAL_PLANE_INCISOR_EDGE, PointEnum.LOWER_MOLAR_MESIAL_TIP, 400),// 24 - OCCLUSAL_PLANE_INCISOR_EDGE, 27 - LOWER_MOLAR_MESIAL_TIP
                    drawLineBetweenPointsExtended(PointEnum.PORION, PointEnum.ORBITALE, 300)  //6 - Porion , 4 - Orbitale
            );

            mainFrame.getChildren().addAll(
                    drawLinesUpToIntersection(PointEnum.GONION, PointEnum.MENTON, PointEnum.NASION, PointEnum.POG), //14 - gonion ,13 menton, 2 - Nasion , 11 - POG // Reverse lines creates triange
                    drawLinesUpToIntersection(PointEnum.NASION, PointEnum.POG, PointEnum.GONION, PointEnum.MENTON), //14 - gonion ,13 menton, 2 - Nasion , 11 - POG  // Reverse lines creates triange
                    drawLinesUpToIntersection(PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.UPPER_INCISOR_ROOT_TIP, PointEnum.NASION, PointEnum.SELLA), // 18 - UPPER_INCISOR_CROWN_TIP , 20 -  UPPER_INCISOR_ROOT_TIP , 2- Nasion , 3 -Sella
                    drawLinesUpToIntersection(PointEnum.LOWER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_ROOT_TIP, PointEnum.MENTON, PointEnum.GONION),//21 - LOWER_INCISOR_CROWN_TIP , 23 - LOWER_INCISOR_ROOT_TIP, 13 Menton , 14 Gonion
                    drawPerpendicularLine(PointEnum.PT, 200), // 17 - PT
                    drawObliqueLine(PointEnum.GONION, PointEnum.MENTON, PointEnum.NASION, PointEnum.POG, PointEnum.PT) // 14 - gonion ,13 menton 2-Nasion , 11 - POG , 17 - PT
            );
        }

        AnalysisFactory analysisFactory = new AnalysisFactory();
        IAnalysisSet analysis = analysisFactory.getAnalysis(selectedAnalysis);
        analysis.setPoints(userDataHandler.getImagePointHashMap());
        analysis.execute();
        HashMap<CalculationTypeEnum, IResult> results = analysis.getResult();
        DisplayIResult tableWindow = new DisplayIResult();
        tableWindow.display(results, "Calculation Results");
    }
}



