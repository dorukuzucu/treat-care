package main.java.drawing;


import java.util.*;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;
import main.java.utils.UserDataHandler;


//TODO HANDLE ADDING TO AND REMOVING FROM USER DATA SCENERIOS
//TODO limit class only for drawing and hold data in a manager class
//TODO Split line and label drawing to different classes
public class MainPaneCircleDrawer {
    private final AnchorPane mainPane;
    private double xShift;
    private double yShift;
    private final HashMap<PointEnum, Circle> points = new HashMap<>();
    private final HashMap<PointEnum, Label> labels = new HashMap<>();
    private final UserDataHandler userDataHandler = UserDataHandler.getInstance();

    public MainPaneCircleDrawer(AnchorPane mainPane, double xShift, double yShift){
        this.mainPane = mainPane;
        this.xShift = xShift;
        this.yShift = yShift;
    }


    public void drawPointAt(PointEnum selectedPoint, double x, double y) {
        //TODO remove point if same id exists
        userDataHandler.addImagePoint(selectedPoint, x, y);

        if (this.points.containsKey(selectedPoint)) {
            Circle oldCircle = this.points.get(selectedPoint);
            this.mainPane.getChildren().remove(oldCircle);
            this.points.remove(selectedPoint);

            Label oldLabel = this.labels.get(selectedPoint);
            this.mainPane.getChildren().remove(oldLabel);
            this.labels.remove(selectedPoint);
        }
        Circle newCircle = this.drawCircle(x, y, selectedPoint.getColor());
        this.points.put(selectedPoint, newCircle);

        this.mainPane.getChildren().add(newCircle);
        this.addLabelToPoint(selectedPoint);
    }

    //TODO move to factory class
    private Circle drawCircle(Double x, Double y, Paint color) {
        Circle circle = new Circle();
        circle.setCenterX(this.xShift + x);
        circle.setCenterY(this.yShift + y);
        circle.setRadius(1.5);
        circle.setMouseTransparent(true);
        circle.setFill(color);
        return circle;
    }

    public void addLabelToPoint(PointEnum point){
        Circle circle = this.points.get(point);
        Label label = this.drawPointLabel(point.getAbbreviation());
        label = this.placeLabel(label,
                circle.getCenterX() + point.getOffsetX(),
                circle.getCenterY() + point.getOffsetY());
        this.labels.put(point, label);
    }

    //TODO move to factory class
    private Label drawPointLabel(String text){
        Label label = new Label();
        label.setText(text);
        label.setTextFill(Color.web("#ffffff"));
        return label;
    }

    public Label placeLabel(Label label, double locationX, double locationY){
        label.setLayoutX(locationX);
        label.setLayoutY(locationY);
        this.mainPane.getChildren().add(label);
        return label;
    }

    public void removePoint(PointEnum point){
        if (!this.points.containsKey(point)) {
            return;
        }

        Circle oldCircle = this.points.get(point);
        this.mainPane.getChildren().remove(oldCircle);
        this.points.remove(point);
        userDataHandler.getImagePointHashMap().remove(point);
        this.removeLabel(point);
    }

    public void hidePoint(PointEnum point){
        if (!this.points.containsKey(point)) {
            return;
        }
        Circle circle = this.points.get(point);
        circle.setVisible(false);

        this.hideLabel(point);
    }

    public void showPoint(PointEnum point){
        if (!this.points.containsKey(point)) {
            return;
        }
        Circle circle = this.points.get(point);
        circle.setVisible(true);

        this.showLabel(point);
    }

    public void removeAllPoints(){
        List<PointEnum> pointsToRemove = new ArrayList<>();
        for (Map.Entry<PointEnum, Circle> point : this.points.entrySet()) {
            pointsToRemove.add(point.getKey());
        }
        for (PointEnum point : pointsToRemove){
            this.removePoint(point);
        }
    }

    public void hideAllPoints(){
        for (Map.Entry<PointEnum, Circle> point : this.points.entrySet()) {
            this.hidePoint(point.getKey());
        }
        this.hideAllLabels();
    }

    public void showAllPoints(){
        for (Map.Entry<PointEnum, Circle> point : this.points.entrySet()) {
            this.showPoint(point.getKey());
        }
        this.showAllLabels();
    }

    public void removeLabel(PointEnum point){
        if (!this.labels.containsKey(point)) {
            return;
        }
        Label oldLabel = this.labels.get(point);
        this.mainPane.getChildren().remove(oldLabel);
        this.labels.remove(point);
    }

    public void hideLabel(PointEnum point){
        if (!this.labels.containsKey(point)) {
            return;
        }
        Label label = this.labels.get(point);
        label.setVisible(false);
    }

    public void showLabel(PointEnum point){
        if (!this.labels.containsKey(point)) {
            return;
        }
        Label label = this.labels.get(point);
        label.setVisible(true);
    }

    public void removeAllLabels(){
        for (Map.Entry<PointEnum, Label> label : this.labels.entrySet()) {
            this.removeLabel(label.getKey());
        }
    }

    public void hideAllLabels(){
        for (Map.Entry<PointEnum, Label> label : this.labels.entrySet()) {
            this.hideLabel(label.getKey());
        }
    }

    public void showAllLabels(){
        for (Map.Entry<PointEnum, Label> label : this.labels.entrySet()) {
            this.showLabel(label.getKey());
        }
    }

    public void displayUserDataPoints() {
        HashMap<PointEnum, ImagePoint> imagePointHashMap = userDataHandler.getImagePointHashMap();
        if (imagePointHashMap == null) {
            return;
        }

        for (Map.Entry<PointEnum, ImagePoint> point : imagePointHashMap.entrySet()) {
            PointEnum pointEnum = point.getKey();
            ImagePoint pointData = point.getValue();
            this.drawPointAt(pointEnum, pointData.getPointX(), pointData.getPointY());
        }
    }
}
