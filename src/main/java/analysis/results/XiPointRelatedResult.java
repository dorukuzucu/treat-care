package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class XiPointRelatedResult extends BaseResult{
    private ImagePoint ruler1Point;
    private ImagePoint ruler2Point;
    private ImagePoint R3Point;
    private ImagePoint R1Point;
    private ImagePoint R2Point;
    private ImagePoint R4Point;

    private ImagePoint Angle1Point;
    private ImagePoint Angle2Point;

    private boolean isAngle;
    private ImagePoint toDistancePointType;

    public XiPointRelatedResult(ImagePoint ruler1Point,
                                ImagePoint ruler2Point,
                                ImagePoint R3Point,
                                ImagePoint R1Point,
                                 ImagePoint R2Point,
                                ImagePoint R4Point,
                                ImagePoint Angle1Point,
                                ImagePoint Angle2Point,
                                ImagePoint toDistancePointType,
                                boolean isAngle,
                                double angle){
        this.ruler1Point = ruler1Point;
        this.ruler2Point = ruler2Point;
        this.R3Point = R3Point;
        this.R1Point = R1Point;
        this.R2Point = R2Point;
        this.R4Point = R4Point;
        this.Angle1Point = Angle1Point;
        this.Angle2Point = Angle2Point;
        this.result = angle;


    }

    public ImagePoint getR3Point() {
        return this.R3Point;
    }

    public ImagePoint getR1Point() {
        return this.R1Point;
    }

    public ImagePoint getR2Point() {
        return this.R2Point;
    }
    public ImagePoint getR4Point() {
        return this.R4Point;
    }
    public ImagePoint getAngle1PointPoint() {
        return this.Angle1Point;
    }
    public ImagePoint getAngle2Point() {
        return this.Angle2Point;
    }
    public boolean getisAngle() {
        return this.isAngle;
    }
    public ImagePoint gettoDistancePointType() {
        return this.toDistancePointType;
    }
}
