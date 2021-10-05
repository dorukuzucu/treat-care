package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class VerticalLineToThreePointAngleResults extends BaseResult{

    private ImagePoint startPoint;
    private ImagePoint endPoint;
    private ImagePoint verticalPoint;
    private ImagePoint angle1Point;
    private ImagePoint angle2Point;

    public VerticalLineToThreePointAngleResults(ImagePoint startPoint, ImagePoint endPoint,
                                 ImagePoint verticalPoint,ImagePoint angle1Point,ImagePoint angle2Point, double angle){

        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.verticalPoint = verticalPoint;
        this.angle1Point = verticalPoint;
        this.angle2Point = verticalPoint;
        this.result = angle;
    }

    public ImagePoint getStartPoint() {
        return this.startPoint;
    }

    public ImagePoint getEndPoint() {
        return this.endPoint;
    }

    public ImagePoint getVerticalPoint() {
        return this.verticalPoint;
    }
    public ImagePoint getAngle1Point() {
        return this.angle1Point;
    }

    public ImagePoint getAngle2Point() {
        return this.angle2Point;
    }
}
