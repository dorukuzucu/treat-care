package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class LineResult extends BaseResult{

    private ImagePoint startPoint;
    private ImagePoint endPoint;

    public LineResult(ImagePoint startPoint, ImagePoint endPoint, double length){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.result = length;
    }

    public double getResult() {
        return result;
    }

    public ImagePoint getStartPoint() {
        return startPoint;
    }

    public ImagePoint getEndPoint() {
        return endPoint;
    }
}
