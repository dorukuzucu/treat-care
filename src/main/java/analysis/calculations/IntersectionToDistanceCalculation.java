package main.java.analysis.calculations;

import main.java.analysis.results.IntersectionToResults;
import main.java.analysis.results.LineResult;
import main.java.analysis.utils.MathUtils;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class IntersectionToDistanceCalculation implements ICalculation {

    PointEnum ruler1type;
    PointEnum ruler2type;
    PointEnum firstPointType;
    PointEnum secondPointType;
    PointEnum thirdPointType;
    PointEnum fourthPointType;
    PointEnum distancePointType;


    public IntersectionToDistanceCalculation(
                           PointEnum ruler1type,
                           PointEnum ruler2type,
                           PointEnum firstPointType,
                           PointEnum secondPointType,
                           PointEnum thirdPointType,
                           PointEnum fourthPointType,
                           PointEnum distancePointType
                           )
    {

        this.ruler1type = ruler1type;
        this.ruler2type = ruler2type;
        this.firstPointType = firstPointType;
        this.secondPointType = secondPointType;
        this.thirdPointType = thirdPointType;
        this.fourthPointType = fourthPointType;
        this.distancePointType = distancePointType;

    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.ruler1type) &&
                points.containsKey(this.ruler2type) &&
                points.containsKey(this.firstPointType) &&
                points.containsKey(this.secondPointType) &&
                points.containsKey(this.thirdPointType) &&
                points.containsKey(this.fourthPointType) &&
                points.containsKey(this.distancePointType));
    }

    @Override
    public IntersectionToResults calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint ruler1 = points.get(this.ruler1type);
        ImagePoint ruler2 = points.get(this.ruler2type);
        ImagePoint firstPoint = points.get(this.firstPointType);
        ImagePoint secondPoint = points.get(this.secondPointType);
        ImagePoint thirdPoint = points.get(this.thirdPointType);
        ImagePoint fourthPoint = points.get(this.fourthPointType);
        ImagePoint distancePoint = points.get(this.distancePointType);


        double a1 = ruler1.getPointX();
        double b1 = ruler1.getPointY();
        double a2 = ruler2.getPointX();
        double b2 = ruler2.getPointY();

        double x1 = firstPoint.getPointX();
        double y1 = firstPoint.getPointY();

        double x2 = secondPoint.getPointX();
        double y2 = secondPoint.getPointY();

        double x3 = thirdPoint.getPointX();
        double y3 = thirdPoint.getPointY();

        double x4 = fourthPoint.getPointX();
        double y4 = fourthPoint.getPointY();

        double Dx1 = distancePoint.getPointX();
        double Dx2 = distancePoint.getPointY();

       // double xIntercept = ((x1*y2 - y1*x2)*(x3-x4) - (x1-x2)*(x3*y4 - y3*x4)) / (x1-x2)*(y3 - y4) - (y1 - y2)*(x3 - x4);
       // double yIntercept = ((x1*y2 - y1*x2) * (y3 - y4) - (y1 - y2) * (x3*y4 - y3*x4)) / (x1-x2)*(y3-y4) - (y1 - y2)*(x3 - x4);


        double xNumerator = (x1*y2 - y1*x2)*(x3-x4) - (x1-x2)*(x3*y4 - y3*x4);
        double xDenominator = (x1-x2)*(y3 - y4) - (y1 - y2)*(x3 - x4);

        double yNumerator = (x1*y2 - y1*x2) * (y3 - y4) - (y1 - y2) * (x3*y4 - y3*x4);
        double yDenominator = (x1-x2)*(y3-y4) - (y1 - y2)*(x3 - x4);

        double xIntersect = xNumerator / xDenominator;
        double yIntersect = yNumerator / yDenominator;

        double distance = MathUtils.distance(xIntersect, yIntersect, Dx1, Dx2);

        double ruler_distance = MathUtils.distance(a1,b1,a2,b2);

        distance = distance * DrawingController.rulerSliderValue / ruler_distance;
        distance = Math.floor(distance * 10)/10;

        return new IntersectionToResults(ruler1,ruler2,firstPoint, secondPoint,thirdPoint,fourthPoint,distancePoint, distance);
    }
}
