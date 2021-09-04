package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class ThreePointAngleResult extends BaseResult{

    private ImagePoint startPoint;
    private ImagePoint centerPoint;
    private ImagePoint endPoint;

    public ThreePointAngleResult(ImagePoint startPoint, ImagePoint centerPoint,
                                 ImagePoint endPoint, double angle){
        this.startPoint = startPoint;
        this.centerPoint = centerPoint;
        this.endPoint = endPoint;
        this.result = angle;
    }

    public ImagePoint getStartPoint() {
        return this.startPoint;
    }

    public ImagePoint getCenterPoint() {
        return this.centerPoint;
    }

    public ImagePoint getEndPoint() {
        return this.endPoint;
    }
}
