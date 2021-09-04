package main.java.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import main.java.analysis.results.IResult;
import main.java.analysis.sets.AnalysisFactory;
import main.java.analysis.sets.IAnalysisSet;
import main.java.analysis.utils.AnalysisEnum;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.database.dao.PatientImageDao;
import main.java.database.entities.ImagePoint;
import main.java.database.entities.Patient;
import main.java.database.entities.PatientImage;
import main.java.utils.PointEnum;
import main.java.utils.TableWindow;
import main.java.utils.UserDataHandler;

import java.io.*;
import java.util.*;

public class DrawingController {

    @FXML
    public AnchorPane mainFrame;

    @FXML
    public Label nameLastNameLabel;
    @FXML
    public Label birthDateLabel;
    @FXML
    public Label genderLabel;
    @FXML
    public Label raceLabel;

    @FXML
    public Button saveButton;

    @FXML
    ImageView cephImageView;

    @FXML
    public ScrollPane scrollCephImage;

    static ImageView alteredCephImage;

    @FXML
    public Button enhance;

    @FXML
    public ComboBox<AnalysisEnum> analyseComboBox;

    @FXML
    private Slider rulerSlider;
    @FXML
    private Label rulerLabel;
    @FXML
    private Slider magnificationSlider;
    @FXML
    private Label magnificationLabel;
    @FXML
    public ListView<PointEnum> anatomicalPoints;

    private final HashMap<PointEnum, Circle> points = new HashMap<>();
    ArrayList<PointEnum> pointList = new ArrayList<>();

    ColorAdjust colorAdjust = new ColorAdjust();

    public void initialize(){
        this.setPatientInfoLabels();
        this.displayPoints();
        this.displayAnalysis();

        this.cephImageView.setOnMouseClicked(e -> this.addPoint(this.anatomicalPoints.getSelectionModel().getSelectedItem(), e.getX(), e.getY()));

        //todo refactor
        //todo when called, get all user related data and display
        //todo for point displaying, call addCircle method, also extract the part where list value is incremented
        UserDataHandler.getInstance().getPatientData();
        PatientImage imageData = UserDataHandler.getInstance().getActivePatientImage();
        if (!(imageData==null)){
            this.setImageToImageView(imageData.getImage());
            this.displayUserDataPoints();
        }
    }

    private void displayUserDataPoints(){
        HashMap<PointEnum, ImagePoint> imagePointHashMap = UserDataHandler.getInstance().getImagePointHashMap();
        if(imagePointHashMap==null){
            return;
        }

        for (Map.Entry<PointEnum, ImagePoint> point : imagePointHashMap.entrySet()){
            PointEnum pointEnum = point.getKey();
            ImagePoint pointData = point.getValue();
            this.addPoint(pointEnum, pointData.getPointX(), pointData.getPointY());
        }
    }

    private void addPoint(PointEnum selectedPoint, double x, double y) {

        Circle newCircle = this.drawCircle(x, y);
        UserDataHandler.getInstance().addImagePoint(selectedPoint, x, y);  // This line overrides value if already inserted
        //TODO remove point if same id exists
        if(this.points.containsKey(selectedPoint)){
            Circle oldCircle = this.points.get(selectedPoint);
            mainFrame.getChildren().remove(oldCircle);
            this.points.remove(selectedPoint);
        }
        this.points.put(selectedPoint, newCircle);

        // Add new circle to imageview and move down in list
        mainFrame.getChildren().add(newCircle);
        int listIndex = this.anatomicalPoints.getSelectionModel().getSelectedIndex();
        this.anatomicalPoints.getSelectionModel().selectIndices(listIndex+1);
    }

    private Circle drawCircle(Double x, Double y){
        double newX = this.anatomicalPoints.getLayoutX()+this.anatomicalPoints.getWidth() + x;
        double newY = this.anatomicalPoints.getLayoutY() + y;

        Circle circle = new Circle();
        circle.setCenterX(newX);
        circle.setCenterY(newY);
        circle.setRadius(3);
        return circle;
    }

    @FXML
    public void BrowseImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files","PNG Files", "*.jpg","*.png"));
        File file = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
        if(file != null) {
            if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")){
                scrollCephImage.setPannable(true);
                clearCeph();
                this.activateSearchedImage(file);
            }
            else {
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
        PatientImage savedPatientImage = patientImageDao.save(UserDataHandler.getInstance().getActivePatient().getId(), imageFile);
        UserDataHandler.getInstance().setActivePatientImage(savedPatientImage);
    }

    private void setImageToImageView(InputStream imageFileInputStream){
        Image image = new Image(imageFileInputStream);
        cephImageView.setImage(image);
        resetAdjustmentsForNewImage();
        alteredCephImage = cephImageView;
    }

    public  void brightnessAdjustments(float brightnessValue){
        colorAdjust.setBrightness( brightnessValue);
        alteredCephImage.setEffect(colorAdjust);
        cephImageView = alteredCephImage;
    }

    public void contrastAdjustments(float contrastValue){
        colorAdjust.setContrast( contrastValue);
        alteredCephImage.setEffect(colorAdjust);
        cephImageView = alteredCephImage;
    }

    public void resetAdjustmentsForNewImage(){
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        EnhanceImageController.brightnessValue = 0;
        EnhanceImageController.contrastValue = 0;
        cephImageView.setEffect(colorAdjust);
    }

    public void clearCeph(){
       // if(cephImage != null)
        //{
            cephImageView.setImage(null);
            //anatomicalPoints.getItems().clear();
            pointList.clear();
       // }
    }

    @FXML
    void EnhanceImagePopUp() throws IOException{
        if(cephImageView.getImage() != null){
            Stage enhanceStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/EnhanceGUI.fxml"));
            enhanceStage.setTitle("EnhanceImageController");
            enhanceStage.setWidth(550);
            enhanceStage.setHeight(250);
            enhanceStage.setResizable(false);
            enhanceStage.setOnCloseRequest(e  -> closeEnhanceGUI());
            enhanceStage.setScene(new Scene(root));
            enhanceStage.show();

            if(enhanceStage.isShowing()){
                enhance.setDisable(true);
            }
        }
    }

    private void closeEnhanceGUI(){
        enhance.setDisable(false);
    }

    @FXML
    private void ChanceRulerSlider(){
        int i = (int)this.rulerSlider.getValue();
        rulerLabel.setText(i + " mm");
    }

    @FXML
    private void ChanceMagnificationSlider(){
        float i = (float)magnificationSlider.getValue();
        magnificationLabel.setText(String.valueOf(String.format("%.1f" ,i )));
    }

    private void displayPoints(){
        pointList.addAll(Arrays.asList(PointEnum.values()));

        anatomicalPoints.getItems().addAll(pointList);
        anatomicalPoints.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        anatomicalPoints.getSelectionModel().select(0);
    }

    private void displayAnalysis(){
        List<AnalysisEnum> analysis = Arrays.asList(AnalysisEnum.values());
        analyseComboBox.getItems().addAll(analysis);
    }

    private void setPatientInfoLabels(){
        Patient activePatient = UserDataHandler.getInstance().getActivePatient();
        this.nameLastNameLabel.setText(activePatient.getName());
        this.birthDateLabel.setText(activePatient.getBirthDay());
        this.genderLabel.setText(activePatient.getGender());
        this.raceLabel.setText(activePatient.getRace());
    }

    //TODO split saving and calculation methods
    public void saveButtonClicked() {
        UserDataHandler.getInstance().saveImagePoints();
        AnalysisEnum selectedAnalysis = this.analyseComboBox.getValue();
        if(selectedAnalysis==null)
            return;
        AnalysisFactory analysisFactory = new AnalysisFactory();
        IAnalysisSet analysis = analysisFactory.getAnalysis(selectedAnalysis);
        analysis.setPoints(UserDataHandler.getInstance().getImagePointHashMap());
        analysis.execute();
        HashMap<CalculationTypeEnum, IResult> results = analysis.getResult();
        TableWindow tableWindow = new TableWindow();
        tableWindow.display(results, "Calculation Results");
    }
}
