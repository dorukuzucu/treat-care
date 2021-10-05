package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class RelativeLineResult extends  BaseResult{
    private ImagePoint ruler1point;
    private ImagePoint ruler2point;
    private ImagePoint startPoint;
    private ImagePoint endPoint;
    private boolean vertical;


    public RelativeLineResult(ImagePoint ruler1point, ImagePoint ruler2point, ImagePoint startPoint, ImagePoint endPoint, double length, boolean vertical){
        this.ruler1point = ruler1point;
        this.ruler2point = ruler2point;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.result = length;
        this.vertical = vertical;
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
