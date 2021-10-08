package main.java.controllers;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.*;
import javafx.scene.Cursor;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
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
import main.java.utils.GetCurrentTime;
import main.java.utils.PointEnum;
import main.java.utils.DisplayIResult;
import main.java.utils.UserDataHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class DrawingController {

    @FXML
    public AnchorPane mainFrame;

    @FXML
    public AnchorPane zoomAnchor;

    @FXML
    public  AnchorPane cephImageAnchor;

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

    static  ImageView alteredZoomImage;

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
    public ListView<PointEnum> anatomicalPoints;


    @FXML
    private Line vLine;
    @FXML
    private Line hLine;


    public Label ruler1Label;
    @FXML
    public Label ruler2Label;
    @FXML
    public Label nasionLabel;
    @FXML
    public Label sellaLabel;
    @FXML
    public Label orbitaleLabel;
    @FXML
    public Label porionLabel;
    @FXML
    public Label basionLabel;
    @FXML
    public Label aLabel;
    @FXML
    public Label bLabel;
    @FXML
    public Label ansLabel;
    @FXML
    public Label pnsLabel;
    @FXML
    public Label mentonLabel;
    @FXML
    public Label gnathionLabel;
    @FXML
    public Label pogonionLabel;
    @FXML
    public Label gonionLabel;
    @FXML
    public Label ramusLabel;
    @FXML
    public Label articulareLabel;
    @FXML
    public Label ptLabel;




    private ArrayList<Line> displayedLine;
    private ArrayList<Circle> displayedCircle;   // Sen bunları galiba değiştirirsin bu yüzden Drawlinebetweenpoints metodu da modifiye edilecek


    private final HashMap<PointEnum, Circle> points = new HashMap<>();


    ArrayList<PointEnum> pointList = new ArrayList<>();

    private UserDataHandler userDataHandler = UserDataHandler.getInstance();


    ColorAdjust colorAdjust = new ColorAdjust();


    @FXML
    public void exportImage (){

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
    public void clearAll()
    {

        deleteAllAbbreviation();
        for(Circle circle : displayedCircle)
        {
            circle = null;     //If allocation is lost ,gives index error
        }
        points.clear();
        mainFrame.getChildren().removeIf(Circle.class::isInstance);
        mainFrame.getChildren().removeIf(Path.class::isInstance);
        anatomicalPoints.getSelectionModel().select(0);

    }


    public void hideAllPointsAndLabels()
    {
        if(hideLabel.getOpacity() == 1)
        {
            for(Object circle : mainFrame.getChildren()) {
                if(circle.getClass().equals(Circle.class))
                {
                    ((Circle) circle).setVisible(false);
                    hideLabel.setOpacity(0.5);
                    hideAbbreviation();
                }
            }
            for(Object path : mainFrame.getChildren()) {
                if(path.getClass().equals(Path.class))
                {
                    ((Path) path).setVisible(false);

                }
            }

        }
        else
        {
            for(Object circle : mainFrame.getChildren()) {
                if(circle.getClass().equals(Circle.class))
                {
                    ((Circle) circle).setVisible(true);
                    hideLabel.setOpacity(1);
                    showAbbreviation();
                }
            }
            for(Object path : mainFrame.getChildren()) {
                if(path.getClass().equals(Path.class))
                {
                    ((Path) path).setVisible(true);
                }
            }
        }


    }


    public void initialize()
    {



        hLine.setVisible(false);
        vLine.setVisible(false);

        rulerSliderValue = rulerSlider.getValue();
        this.setPatientInfoLabels();
        this.displayPoints();
        this.displayAnalysis();

        displayedCircle = new ArrayList(pointList.size()-1);  // Allocating for circles
        for (int i = 0; i < pointList.size()-1; i++) // "-1" excluding "Finished"   circle
            displayedCircle.add(null);



        cephImageView.setOnMouseEntered(e -> {

            if(cephImageView.getImage()!= null) {
                hLine.setVisible(true);
                vLine.setVisible(true);

                zoomedImage.setVisible(true);
                zoomSliderValue = ZoomSlider.getValue();

            }
        });

        cephImageView.setOnMouseMoved(e -> {


            if(cephImageView.getImage() != null) {


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

                if(cephImageView.getImage()!=null)
                {
                    hLine.setVisible(false);
                    vLine.setVisible(false);
                    zoomedImage.setVisible(false);


                }
        });

        cephImageView.setOnMouseClicked(mouseEvent ->
        {
            if(cephImageView.getImage()!=null) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) // ADD POINTS
                {

                    int listIndex = this.anatomicalPoints.getSelectionModel().getSelectedIndex();
                    if (listIndex <= pointList.size() - 2)  //
                    {
                        PointEnum selectedPoint = this.anatomicalPoints.getSelectionModel().getSelectedItem();
                        addPoint(selectedPoint, mouseEvent.getX(), mouseEvent.getY());
                        mainFrame.getChildren().add(displayedCircle.get(listIndex));
                        this.anatomicalPoints.getSelectionModel().selectIndices(listIndex + 1);

                    }


                }
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) // DELETE POINTS
                {
                    int listIndex = this.anatomicalPoints.getSelectionModel().getSelectedIndex();

                    if (listIndex > 0) {
                        mainFrame.getChildren().remove(displayedCircle.get(listIndex - 1));
                        displayedCircle.set(listIndex - 1, null);
                        // DB den de silinecek

                        this.anatomicalPoints.getSelectionModel().selectIndices(listIndex - 1);
                        deleteAbbreviation(listIndex);


                    }
                }

                if (mouseEvent.getButton().equals(MouseButton.MIDDLE)) {
                  //EMPTY

                }
            }
        });




        //todo refactor
        //todo when called, get all user related data and display
        //todo for point displaying, call addCircle method, also extract the part where list value is incremented
        userDataHandler.getPatientData();
        PatientImage imageData = userDataHandler.getActivePatientImage();
        if (!(imageData==null)){
            this.setImageToImageView(imageData.getImage());
            this.displayUserDataPoints();
        }
    }





    private void deleteAbbreviation(int listIndex)
    {
        if(listIndex == 1)
        {
            ruler1Label.setText("");
        }
        if(listIndex == 2)
        {
            ruler2Label.setText("");
        }
        if(listIndex == 3)
        {
            nasionLabel.setText("");
        }
        if(listIndex == 4)
        {
            sellaLabel.setText("");
        }
        if(listIndex == 5)
        {
            orbitaleLabel.setText("");
        }
        if(listIndex == 6)
        {
            basionLabel.setText("");
        }
        if(listIndex == 7)
        {
            porionLabel.setText("");
        }
        if(listIndex == 8)
        {
            ansLabel.setText("");
        }
        if(listIndex == 9)
        {
            pnsLabel.setText("");
        }
        if(listIndex == 10)
        {
            aLabel.setText("");
        }
        if(listIndex == 11)
        {
            bLabel.setText("");
        }
        if(listIndex == 12)
        {
            pogonionLabel.setText("");
        }
        if(listIndex == 13)
        {
            gnathionLabel.setText("");
        }
        if(listIndex == 14)
        {
            mentonLabel.setText("");
        }
        if(listIndex == 15)
        {
            gonionLabel.setText("");
        }
        if(listIndex == 16)
        {
            ramusLabel.setText("");
        }
        if(listIndex == 17)
        {
            articulareLabel.setText("");
        }
        if(listIndex == 18)
        {
            ptLabel.setText("");
        }
    }
    private  void deleteAllAbbreviation()
    {
        ruler1Label.setText("");
        ruler2Label.setText("");
        nasionLabel.setText("");
        sellaLabel.setText("");
        orbitaleLabel.setText("");
        porionLabel.setText("");
        basionLabel.setText("");
        aLabel.setText("");
        bLabel.setText("");
        ansLabel.setText("");
        pnsLabel.setText("");
        mentonLabel.setText("");
        gnathionLabel.setText("");
        pogonionLabel.setText("");
        gonionLabel.setText("");
        ramusLabel.setText("");
        articulareLabel.setText("");
        ptLabel.setText("");
    }

    private  void displayAbbreviationMethod(Circle circle)
    {
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() ==  "Ruler1")
        {
            displayAbbreviation(ruler1Label,"D1",circle,-25,-10 , Color.CORAL);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Ruler2")
        {
            displayAbbreviation(ruler2Label,"D2",circle,-25,-10,Color.CORAL);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Nasion")
        {
            displayAbbreviation(nasionLabel,"N",circle,-18,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Sella")
        {
            displayAbbreviation(sellaLabel,"S",circle,-8,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Orbitale")
        {
            displayAbbreviation(orbitaleLabel,"Or",circle,-8,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Porion")
        {
            displayAbbreviation(porionLabel,"Po",circle,-18,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Basion")
        {
            displayAbbreviation(basionLabel,"Ba",circle,-20,-5,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "ANS")
        {
            displayAbbreviation(ansLabel,"ANS",circle,-8,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "PNS")
        {
            displayAbbreviation(pnsLabel,"PNS",circle,-8,-20,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "A point")
        {
            displayAbbreviation(aLabel,"A",circle,5,-15,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "B point")
        {
            displayAbbreviation(bLabel,"B",circle,10,-10,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Pogonion")
        {
            displayAbbreviation(pogonionLabel,"Pog",circle,8,-10,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Menton")
        {
            displayAbbreviation(mentonLabel,"Me",circle,-8,8,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Gnathion")
        {
            displayAbbreviation(gnathionLabel,"Gn",circle,8,3,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Gonion")
        {
            displayAbbreviation(gonionLabel,"Go",circle,-23,-5,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Ramus point")
        {
            displayAbbreviation(ramusLabel,"R",circle,-15,-8,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Articulare")
        {
            displayAbbreviation(articulareLabel,"Ar",circle,-19,0,Color.RED);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "PT point")
        {
            displayAbbreviation(ptLabel,"Pt",circle,-8,-20,Color.RED);
        }


    }

    private void hideAbbreviation()
    {
       ruler1Label.setVisible(false);
       ruler2Label.setVisible(false);
        nasionLabel.setVisible(false);
        sellaLabel.setVisible(false);
        orbitaleLabel.setVisible(false);
        porionLabel.setVisible(false);
        basionLabel.setVisible(false);
        aLabel.setVisible(false);
        bLabel.setVisible(false);
        ansLabel.setVisible(false);
        pnsLabel.setVisible(false);
         mentonLabel.setVisible(false);
        gnathionLabel.setVisible(false);
        pogonionLabel.setVisible(false);
        gonionLabel.setVisible(false);
        ramusLabel.setVisible(false);
        articulareLabel.setVisible(false);
        ptLabel.setVisible(false);

    }
    private void showAbbreviation()
    {
        ruler1Label.setVisible(true);
        ruler2Label.setVisible(true);
        nasionLabel.setVisible(true);
        sellaLabel.setVisible(true);
        orbitaleLabel.setVisible(true);
        porionLabel.setVisible(true);
        basionLabel.setVisible(true);
        aLabel.setVisible(true);
        bLabel.setVisible(true);
        ansLabel.setVisible(true);
        pnsLabel.setVisible(true);
        mentonLabel.setVisible(true);
        gnathionLabel.setVisible(true);
        pogonionLabel.setVisible(true);
        gonionLabel.setVisible(true);
        ramusLabel.setVisible(true);
        articulareLabel.setVisible(true);
        ptLabel.setVisible(true);
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
        Patient activePatient = userDataHandler.getActivePatient();
        this.nameLastNameLabel.setText(activePatient.getName());
        this.genderLabel.setText(activePatient.getGender());
        this.raceLabel.setText(activePatient.getRace());
        this.currentDateLabel.setText(GetCurrentTime.now());


        LocalDate patientBirthDate = LocalDate.parse(activePatient.getBirthDay());
        this.birthDateLabel.setText(patientBirthDate.getDayOfMonth()+"."+patientBirthDate.getMonthValue()+"."+patientBirthDate.getYear());

        Period period = Period.between(patientBirthDate, LocalDate.now());
        if(period.getMonths() >0)
        {
            this.currentAgeLabel.setText(period.getYears() + " Years " + period.getMonths() + " Months");
        }
        else
        {
            this.currentAgeLabel.setText(period.getYears() + " Years old");
        }


    }

    private void displayUserDataPoints(){
        HashMap<PointEnum, ImagePoint> imagePointHashMap = userDataHandler.getImagePointHashMap();
        if(imagePointHashMap==null){
            return;
        }
        System.out.println(points.size());

        for (Map.Entry<PointEnum, ImagePoint> point : imagePointHashMap.entrySet()){
            PointEnum pointEnum = point.getKey();
            ImagePoint pointData = point.getValue();
            this.addPoint(pointEnum, pointData.getPointX(), pointData.getPointY());

            //ARŞİVDEN KAYIT AÇILINCA POİNT LER TEKRAR AKTİF OLUYOR AMA DİSPLAY EDEMİYOR BURDA SIKINTI VAR
        }
    }



  private void addPoint(PointEnum selectedPoint, double x, double y) {


      Circle newCircle = this.drawCircle(x, y);
      userDataHandler.addImagePoint(selectedPoint, x, y);  // This line overrides value if already inserted
      //TODO remove point if same id exists
      if(this.points.containsKey(selectedPoint)){
          Circle oldCircle = this.points.get(selectedPoint);
          mainFrame.getChildren().remove(oldCircle);
          this.points.remove(selectedPoint);

      }

      int listIndex = this.anatomicalPoints.getSelectionModel().getSelectedIndex();
      this.points.put(selectedPoint, newCircle);  // points DB tarafını , displayedcirles GUI tarafını oluşturuyor.

        displayedCircle.set(listIndex,newCircle);


  }


    private Circle drawCircle(Double x, Double y){
        double newX = this.anatomicalPoints.getLayoutX()+this.anatomicalPoints.getWidth() + x;
        double newY = this.anatomicalPoints.getLayoutY() + y;

        Circle circle = new Circle();
        circle.setCenterX(newX);
        circle.setCenterY(newY);
        circle.setRadius(1.5);

        displayAbbreviationMethod(circle);
        changeCirclesColor(circle);

        return circle;
    }

    private void displayAbbreviation(Label abbreviationLabel, String Abbreviation, Circle circle, int xOffset, int yOffset, Paint fill)
    {

        abbreviationLabel.setTextFill(Color.web("#ffffff"));
        abbreviationLabel.setText(Abbreviation);
        abbreviationLabel.setLayoutX(circle.getCenterX() + xOffset);
        abbreviationLabel.setLayoutY(circle.getCenterY() + yOffset);
        circle.setFill(fill);
        circle.setMouseTransparent(true);
        abbreviationLabel.setMouseTransparent(true);  // ALLOWS USER TO BE ABLE TO PLACE POINTS ON OVERLAPPING LABEL AND CIRCLE,JUST IN CASE..

    }

    private void changeCirclesColor(Circle circle)
    {
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Incisor crown tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Incisor gingival border")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Incisor root tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Incisor crown tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Incisor gingival border")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Incisor root tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Molar Distal Surface")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Molar Distal Root Tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Upper Molar Mesial Cusp Tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }

        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Molar Distal Surface")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Molar Mesial Cusp Tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Lower Molar Distal Root Tip")
        {
            changeToothCirclesColor(circle,Color.ORANGE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Midpoint of U1 Tip - L1 Tip")
        {
            changeToothCirclesColor(circle,Color.BLACK);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue Glabella")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue Nasion")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Bridge of Nose")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Tip of Nose")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Midpoint of Columella")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Subnasale")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue A point")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Most forward position of upper Lip")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Bottommost position of upper lip")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Topmost position of lower lip")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Most forward position of lower Lip")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue B point")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue Pogonion")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue Gnathion")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }
        if(this.anatomicalPoints.getSelectionModel().getSelectedItem().toString() == "Soft Tissue Menton")
        {
            changeToothCirclesColor(circle,Color.WHITE);
        }


    }

    private void changeToothCirclesColor(Circle circle, Paint fill)
    {
        circle.setFill(fill);
        circle.setMouseTransparent(true);
    }



    @FXML
    public void BrowseImage() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files","PNG Files","JFIF Files", "*.jpg","*.png","*.jfif"));
        File file = fileChooser.showOpenDialog(mainFrame.getScene().getWindow());
        if(file != null) {
            if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")|| file.getName().toLowerCase().endsWith(".jfif")){
                clearAll();
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
        PatientImage savedPatientImage = patientImageDao.newImage(userDataHandler.getActivePatient().getId(), imageFile);
        userDataHandler.setActivePatientImage(savedPatientImage);
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
        alteredZoomImage.setEffect(colorAdjust);

    }

    public void contrastAdjustments(float contrastValue){
        colorAdjust.setContrast( contrastValue);
        alteredCephImage.setEffect(colorAdjust);
        cephImageView = alteredCephImage;
        alteredZoomImage.setEffect(colorAdjust);
    }

    public void resetAdjustmentsForNewImage(){
        colorAdjust.setBrightness(0);
        colorAdjust.setContrast(0);
        EnhanceImageController.brightnessValue = 0;
        EnhanceImageController.contrastValue = 0;
        cephImageView.setEffect(colorAdjust);
        if(alteredZoomImage != null) {     // if not null , gives error
            alteredZoomImage.setEffect(colorAdjust);
        }

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
        rulerSliderValue = i;
    }

    @FXML
    private void ChanceZoomSlider(){
        float i = (float)ZoomSlider.getValue();
        //zoomLabel.setText("x" + (String.format("%.1f" ,i )));
    }


    private  Path drawLineBetweenPointsExactDistance(int first,int second)
    {
        Path path = new Path();

        double x1;
        double y1;
        double x2;
        double y2;

        x1 = displayedCircle.get(first).getCenterX();
        y1 = displayedCircle.get(first).getCenterY();

        x2 = displayedCircle.get(second).getCenterX();
        y2 = displayedCircle.get(second).getCenterY();

        path.getElements().add(new MoveTo(x1,y1));
        path.getElements().add(new LineTo(x2,y2));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;
    }

    private Path  drawLineBetweenPointsExtended(int first,int second , int lineDistance)
    {
        Path path = new Path();

            double x1;
            double y1;
            double x2;
            double y2;
            double x3;
            double y3;


            x1 = displayedCircle.get(first).getCenterX();
            y1 = displayedCircle.get(first).getCenterY();

            x2 = displayedCircle.get(second).getCenterX();
            y2 = displayedCircle.get(second).getCenterY();

            double d = Math.sqrt((x2-x1) * (x2-x1) + (y2 - y1) * (y2 - y1));

            double r = lineDistance/d;

            x3 = r * x2 + (1 - r) * x1;  //Some Cool Calculations
            y3 = r * y2 + (1 - r) * y1;


            path.getElements().add(new MoveTo(displayedCircle.get(first).getCenterX(),displayedCircle.get(first).getCenterY()));
            path.getElements().add(new LineTo(x3,y3));
            path.setStroke(Color.YELLOW);
            path.setMouseTransparent(true);

            return path;


    }

    private Path drawLinesUpToIntersection(int first,int second,int third,int fourth)
    {
        Path path = new Path();

        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double x4;
        double y4;

        x1 = displayedCircle.get(first).getCenterX();
        y1 = displayedCircle.get(first).getCenterY();

        x2 = displayedCircle.get(second).getCenterX();
        y2 = displayedCircle.get(second).getCenterY();

        x3 = displayedCircle.get(third).getCenterX();
        y3 = displayedCircle.get(third).getCenterY();

        x4 = displayedCircle.get(fourth).getCenterX();
        y4 = displayedCircle.get(fourth).getCenterY();

        double d1 =  (x1 - x2) * (y3 - y4);
        double d2 = (y1 - y2) * (x3 - x4);
        double d  = (d1) - (d2);

        double u1 = (x1 * y2 - y1 * x2);
        double u4 = (x3 * y4 - y3 * x4);

        double u2x =  (x3 - x4);
        double u3x = (x1 - x2);
        double u2y = (y3 - y4);
        double u3y = (y1 - y2);

        double xIntercept = (u1 * u2x - u3x * u4) / d;
        double yIntercept = (u1 * u2y - u3y * u4) / d;

        path.getElements().add(new MoveTo(displayedCircle.get(first).getCenterX(),displayedCircle.get(first).getCenterY()));
        path.getElements().add(new LineTo(xIntercept,yIntercept));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;

    }

    private Path drawPerpendicularLine(int first , int lineDistance)
    {
        Path path = new Path();

        double x1;
        double y1;


        x1 = displayedCircle.get(first).getCenterX();
        y1 = displayedCircle.get(first).getCenterY();



        path.getElements().add(new MoveTo(displayedCircle.get(first).getCenterX(),displayedCircle.get(first).getCenterY()));
        path.getElements().add(new LineTo(x1,y1 + lineDistance));   // same x value , different y value , defines perpendicular line
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;

    }

    private Path drawObliqueLine(int first ,int second,int third , int fourth , int fifth)
    {

        Path path = new Path();
        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;
        double x4;
        double y4;

        x1 = displayedCircle.get(first).getCenterX();
        y1 = displayedCircle.get(first).getCenterY();

        x2 = displayedCircle.get(second).getCenterX();
        y2 = displayedCircle.get(second).getCenterY();

        x3 = displayedCircle.get(third).getCenterX();
        y3 = displayedCircle.get(third).getCenterY();

        x4 = displayedCircle.get(fourth).getCenterX();
        y4 = displayedCircle.get(fourth).getCenterY();

        double d1 =  (x1 - x2) * (y3 - y4);
        double d2 = (y1 - y2) * (x3 - x4);
        double d  = (d1) - (d2);

        double u1 = (x1 * y2 - y1 * x2);
        double u4 = (x3 * y4 - y3 * x4);

        double u2x =  (x3 - x4);
        double u3x = (x1 - x2);
        double u2y = (y3 - y4);
        double u3y = (y1 - y2);

        double xIntercept = (u1 * u2x - u3x * u4) / d;
        double yIntercept = (u1 * u2y - u3y * u4) / d;

        path.getElements().add(new MoveTo(displayedCircle.get(fifth).getCenterX(),displayedCircle.get(fifth).getCenterY()));
        path.getElements().add(new LineTo(xIntercept,yIntercept));
        path.setStroke(Color.YELLOW);
        path.setMouseTransparent(true);

        return path;

    }

    //TODO split saving and calculation methods
    public void saveButtonClicked() {
        userDataHandler.saveImagePoints();
        AnalysisEnum selectedAnalysis = this.analyseComboBox.getValue();
        if(selectedAnalysis==null)
            return;

        if(selectedAnalysis.toString() =="STEINER")
        {
                         // Senin için Hangi numara Hangi pointEnum yazdım reis!!!!!!!!!!!!!!

                         //Sen neden yazmadın dersen inşa edeceğin mimariye uymayabilir diye ben yapmak istemedim.

            mainFrame.getChildren().removeIf(Path.class::isInstance);  // clear before to avoid multiple path creating
            mainFrame.getChildren().addAll(drawLineBetweenPointsExtended(2,3,300), // 2 -Sella  3 - Nasion
                    drawLineBetweenPointsExtended(13,14,300),  // 13-Menton 14Gonion
                    drawLineBetweenPointsExtended(24,27,300), // 24 -OCCLUSAL_PLANE_INCISOR_EDGE 27 -LOWER_MOLAR_MESIAL_TIP
                    drawLineBetweenPointsExtended(4,6,300)); // 4 - Orbitale , 6 - Porion

            mainFrame.getChildren().addAll(
                    drawLinesUpToIntersection(18,20,2,3), // 18 -UPPER_INCISOR_CROWN_TIP , 20 -UPPER_INCISOR_ROOT_TIP
                    drawLinesUpToIntersection(21,23,13,14),// 21 - LOWER_INCISOR_CROWN_TIP , 23 - LOWER_INCISOR_ROOT_TIP
                    drawLinesUpToIntersection(7,8,13,14)  // 7 ANS- 8 PNS- 14 GONION- 13 MENTON

            );
            mainFrame.getChildren().addAll(
                    drawLineBetweenPointsExactDistance(35,43), // 35 - MIDPOINT_OF_COLUMELLA - 43 ST_Pogonion
                    drawLineBetweenPointsExactDistance(3,16), // 3 - Sella , 16 -  Articulare
                    drawLineBetweenPointsExactDistance(16,14) // 16 - Articulare , 14 -  Gonion
                    );
        }
        if(selectedAnalysis.toString() =="RICKETS")
        {
            mainFrame.getChildren().removeIf(Path.class::isInstance);  // clear before to avoid multiple path creating

            mainFrame.getChildren().addAll(
                    drawLineBetweenPointsExactDistance(34,43),  //34 - TIP_OF_NOSE , 43 - ST_pogonion
                    drawLineBetweenPointsExtended(2,5,450), // 5 - Basion, 2- Nasion
                    drawLineBetweenPointsExtended(24,27,400),// 24 - OCCLUSAL_PLANE_INCISOR_EDGE, 27 - LOWER_MOLAR_MESIAL_TIP
                    drawLineBetweenPointsExtended(6,4,300)  //6 - Porion , 4 - Orbitale
            );


            mainFrame.getChildren().addAll(
                    drawLinesUpToIntersection(14,13,2,11), //14 - gonion ,13 menton, 2 - Nasion , 11 - POG // Reverse lines creates triange
                    drawLinesUpToIntersection(2,11,14,13), //14 - gonion ,13 menton, 2 - Nasion , 11 - POG  // Reverse lines creates triange
                    drawLinesUpToIntersection(18,20,2,3), // 18 - UPPER_INCISOR_CROWN_TIP , 20 -  UPPER_INCISOR_ROOT_TIP , 2- Nasion , 3 -Sella
                    drawLinesUpToIntersection(21,23,13,14),//21 - LOWER_INCISOR_CROWN_TIP , 23 - LOWER_INCISOR_ROOT_TIP, 13 Menton , 14 Gonion
                    drawPerpendicularLine(17,200), // 17 - PT
                    drawObliqueLine(14 ,13 ,2,11,17) // 14 - gonion ,13 menton 2-Nasion , 11 - POG , 17 - PT
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



