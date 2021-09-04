package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class FourPointAngleResult extends BaseResult{
    private ImagePoint firstVectorStartPoint;
    private ImagePoint firstVectorEndPoint;
    private ImagePoint secondVectorStartPoint;
    private ImagePoint secondVectorEndPoint;

    public FourPointAngleResult(ImagePoint firstVectorStartPoint, ImagePoint firstVectorEndPoint, ImagePoint secondVectorStartPoint, ImagePoint secondVectorEndPoint, double angle){
        this.firstVectorStartPoint = firstVectorStartPoint;
        this.firstVectorEndPoint = firstVectorEndPoint;
        this.secondVectorStartPoint = secondVectorStartPoint;
        this.secondVectorEndPoint = secondVectorEndPoint;
        this.result = angle;
    }

    public ImagePoint getFirstVectorEndPoint() {
        return this.firstVectorEndPoint;
    }

    public ImagePoint getFirstVectorStartPoint() {
        return this.firstVectorStartPoint;
    }

    public ImagePoint getSecondVectorEndPoint() {
        return this.secondVectorEndPoint;
    }

    public ImagePoint getSecondVectorStartPoint() {
        return this.secondVectorStartPoint;
    }

}
