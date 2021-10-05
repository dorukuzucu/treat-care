package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class PointToPerpendicularLineResults extends BaseResult{

    private ImagePoint ruler1point;
    private ImagePoint ruler2point;
    private ImagePoint startPoint;
    private ImagePoint endPoint;
    private ImagePoint perpendicular_point;
    private ImagePoint molar_distal_point;


    public PointToPerpendicularLineResults(ImagePoint ruler1point,ImagePoint ruler2point,ImagePoint startPoint, ImagePoint endPoint,
                                 ImagePoint perpendicular_point, ImagePoint molar_distal_point,double distance){
        this.ruler1point = ruler1point;
        this.ruler2point = ruler2point;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.perpendicular_point = perpendicular_point;
        this.molar_distal_point = molar_distal_point;
        this.result = distance;

    }

    public ImagePoint getruler1point() {
        return this.ruler1point;
    }

    public ImagePoint getruler2point() {
        return this.ruler2point;
    }

    public ImagePoint getstartPoint() {
        return this.startPoint;
    }
    public ImagePoint getendPoint() {
        return this.endPoint;
    }
    public ImagePoint perpendicular_point() {
        return this.perpendicular_point;
    }
    public ImagePoint molar_distal_point() {
        return this.molar_distal_point;
    }
}
