package main.java.analysis.results;

import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

public class PerpendicularToLineResult extends BaseResult{

    private ImagePoint ruler1point;
    private ImagePoint ruler2point;
    private ImagePoint startPointLine;
    private ImagePoint EndPointLine;
    private ImagePoint PerpendicularTolinePoint;

    public PerpendicularToLineResult(ImagePoint ruler1point ,ImagePoint ruler2point,ImagePoint startPointLine, ImagePoint EndPointLine,
                                 ImagePoint PerpendicularTolinePoint, double distance){

        this.ruler1point = ruler1point;
        this.ruler2point = ruler2point;
        this.startPointLine = startPointLine;
        this.EndPointLine = EndPointLine;
        this.PerpendicularTolinePoint = PerpendicularTolinePoint;
        this.result = distance;
    }

    public ImagePoint getStartPointIP() {
        return this.startPointLine;
    }

    public ImagePoint getEndPointIP() {
        return this.EndPointLine;
    }

    public ImagePoint getPerpendicularTolinePointIP() {
        return this.PerpendicularTolinePoint;
    }
}

