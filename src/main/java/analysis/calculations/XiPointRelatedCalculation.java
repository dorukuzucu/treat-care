package main.java.analysis.calculations;

import main.java.analysis.results.IResult;
import main.java.analysis.results.XiPointRelatedResult;
import main.java.analysis.utils.MathUtils;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class XiPointRelatedCalculation implements ICalculation {
    PointEnum ruler1type;
    PointEnum ruler2type;
    PointEnum R3PointType;
    PointEnum R2PointType;
    PointEnum R1Pointtype;
    PointEnum R4Pointtype;

    PointEnum Angle1Pointtype;
    PointEnum Angle2Pointtype;

    boolean isAngle;

    PointEnum toDistancePointType;

    public XiPointRelatedCalculation(PointEnum ruler1type,
                                     PointEnum ruler2type,
                                     PointEnum R3PointType,
                                     PointEnum R2PointType,
                                     PointEnum R1Pointtype,
                                     PointEnum R4Pointtype,
                                     PointEnum Angle1Pointtype,
                                     PointEnum Angle2Pointtype,
                                     boolean isAngle,
                                     PointEnum toDistancePointType)
    {
        this.ruler1type = ruler1type;
        this.ruler2type = ruler2type;
        this.R3PointType = R3PointType;
        this.R2PointType = R2PointType;
        this.R1Pointtype = R1Pointtype;
        this.R4Pointtype = R4Pointtype;

        this.Angle1Pointtype = Angle1Pointtype;
        this.Angle2Pointtype = Angle2Pointtype;

        this.isAngle = isAngle;

        this.toDistancePointType = toDistancePointType;
    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.ruler1type) &&
                points.containsKey(this.ruler2type) &&
                points.containsKey(this.R3PointType) &&
                points.containsKey(this.R2PointType) &&
                points.containsKey(this.R1Pointtype) &&
                points.containsKey(this.R4Pointtype) &&
                points.containsKey(this.Angle1Pointtype) &&
                points.containsKey(this.Angle2Pointtype)&&
                points.containsKey(this.toDistancePointType)
                );
    }

    @Override
    public IResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint ruler1 = points.get(this.ruler1type);
        ImagePoint ruler2 = points.get(this.ruler2type);
        ImagePoint R3 = points.get(this.R3PointType);
        ImagePoint R2 = points.get(this.R2PointType);
        ImagePoint R1 = points.get(this.R1Pointtype);
        ImagePoint R4 = points.get(this.R4Pointtype);

        ImagePoint A1 = points.get(this.Angle1Pointtype);
        ImagePoint A2 = points.get(this.Angle2Pointtype);

        ImagePoint D = points.get(this.toDistancePointType);

        double value = 0;

        double xNumerator = (R2.getPointX()* R4.getPointY() - R3.getPointY()*R1.getPointX()) * (R2.getPointX() - R1.getPointX()) - (R2.getPointX() - R1.getPointX()) * (R2.getPointX()*R3.getPointY() - R4.getPointY()* R1.getPointX());
        double xDenominator = (R2.getPointX() - R1.getPointX()) * (R4.getPointY() - R3.getPointY()) - (R3.getPointY()-R4.getPointY())*(R2.getPointX() - R1.getPointX());

        double yNumerator = (R2.getPointX()*R4.getPointY() - R3.getPointY()*R1.getPointX()) * (R4.getPointY() - R3.getPointY()) - (R3.getPointY() - R4.getPointY()) * (R2.getPointX()*R3.getPointY() - R4.getPointY()*R1.getPointX());
        double yDenominator = (R2.getPointX() - R1.getPointX()) * (R4.getPointY() - R3.getPointY()) - (R3.getPointY() - R4.getPointY())*(R2.getPointX() - R1.getPointX());

        double xIntercept = xNumerator/xDenominator;
        double yIntercept = yNumerator / yDenominator;

        if(isAngle == true)
        {

            value =  MathUtils.angle(xIntercept, yIntercept,
                    A1.getPointX(), A1.getPointY(),
                    A2.getPointX(), A2.getPointY());
            value = 180 - value;
            value = Math.floor(value * 10)/10;
        }
        else
        {
            double a1 = ruler1.getPointX();
            double b1 = ruler1.getPointY();
            double a2 = ruler2.getPointX();
            double b2 = ruler2.getPointY();

            double x = D.getPointX();
            double y = D.getPointY();

            double ruler_distance = MathUtils.distance(a1,b1,a2,b2);
            double distance = MathUtils.distance(x, y, xIntercept, yIntercept);

            value= distance * DrawingController.rulerSliderValue / ruler_distance;
            value = Math.floor(value * 10)/10;
        }


        return new XiPointRelatedResult(ruler1,ruler2,R3, R2, R1,R4,A1,A2,D,isAngle, value);
    }

}
