package main.java.analysis.calculations;

import main.java.analysis.results.IResult;
import main.java.analysis.results.ThreePointAngleResult;
import main.java.analysis.results.VerticalLineToThreePointAngleResults;
import main.java.analysis.utils.MathUtils;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class VerticalLineToThreePointAngleCalculation implements ICalculation {
    PointEnum startPointType;
    PointEnum endPointType;
    PointEnum verticalPointType;
    PointEnum angle1PointType;
    PointEnum angle2PointType;

    public VerticalLineToThreePointAngleCalculation(PointEnum startPointType, PointEnum endPointType, PointEnum vericalPointType,
                                                    PointEnum angle1PointType,PointEnum angle2PointType)
    {
        this.startPointType = startPointType;
        this.endPointType = endPointType;
        this.verticalPointType = vericalPointType;
        this.angle1PointType = angle1PointType;
        this.angle2PointType = angle2PointType;

    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.startPointType) &&
                points.containsKey(this.endPointType) &&
                points.containsKey(this.verticalPointType)&& points.containsKey(this.angle1PointType)&& points.containsKey(this.angle2PointType));
    }

    @Override
    public IResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint startPoint = points.get(this.startPointType);
        ImagePoint endPoint = points.get(this.endPointType);
        ImagePoint verticalPoint = points.get(this.verticalPointType);
        ImagePoint angle1Point = points.get(this.angle1PointType);
        ImagePoint angle2Point = points.get(this.angle2PointType);

        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;

        x1 = startPoint.getPointX();
        y1 = startPoint.getPointY();

        x2= endPoint.getPointX();
        y2 = endPoint.getPointY();

        x3= verticalPoint.getPointX();
        y3 = verticalPoint.getPointY();

        double k = ((y2-y1) * (x3-x1) - (x2-x1) * (y3-y1)) / ((y2-y1) * (y2 -y1) + (x2-x1) * (x2-x1));
       double xIntercept = x3 - k * (y2-y1);
        double yIntercept = y3 + k * (x2-x1);

        double angle =  MathUtils.angle(xIntercept,yIntercept,
                angle1Point.getPointX(), angle1Point.getPointY(),
                angle2Point.getPointX(), angle2Point.getPointY());

        return new VerticalLineToThreePointAngleResults(startPoint, endPoint,verticalPoint,angle1Point,angle2Point, angle);
    }

}
