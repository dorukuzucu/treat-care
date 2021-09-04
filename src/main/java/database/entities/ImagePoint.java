package main.java.database.entities;

public class ImagePoint {
    Double id;
    Double imageId;
    String pointName;
    Double pointX;
    Double pointY;

    public ImagePoint(Double id, Double imageId, String pointName, Double pointX, Double pointY){
        this.id = id;
        this.imageId = imageId;
        this.pointName = pointName;
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getId() {
        return this.id;
    }

    public void setImageId(Double imageId) {
        this.imageId = imageId;
    }

    public Double getImageId() {
        return this.imageId;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointX(Double pointX) {
        this.pointX = pointX;
    }

    public Double getPointX() {
        return pointX;
    }

    public void setPointY(Double pointY) {
        this.pointY = pointY;
    }

    public Double getPointY() {
        return pointY;
    }

    public String toString(){
        return String.format("id:%.0f, image_id:%.0f, name:%s, (x,y)=(%f,%f)",id,imageId,pointName,pointX,pointY);
    }
}
