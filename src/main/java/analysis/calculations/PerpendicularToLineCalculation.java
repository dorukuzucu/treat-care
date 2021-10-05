package main.java.analysis.calculations;

import main.java.analysis.results.FourPointAngleResult;
import main.java.analysis.results.PerpendicularToLineResult;
import main.java.analysis.utils.MathUtils;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class PerpendicularToLineCalculation implements ICalculation{

    PointEnum ruler1point;
    PointEnum ruler2point;
    PointEnum startPointLine;
    PointEnum EndPointLine;
    PointEnum PerpendicularToPointLine;

    public PerpendicularToLineCalculation(PointEnum ruler1point, PointEnum ruler2point,PointEnum startPointLine, PointEnum EndPointLine, PointEnum PerpendicularTolinePoint){
        this.ruler1point = ruler1point;
        this.ruler2point = ruler2point;
        this.startPointLine = startPointLine;
        this.EndPointLine = EndPointLine;
        this.PerpendicularToPointLine = PerpendicularTolinePoint;
    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.startPointLine) &&
                points.containsKey(this.EndPointLine) &&
                points.containsKey(this.PerpendicularToPointLine)&&
                points.containsKey(this.ruler1point)&&
                points.containsKey(this.ruler2point)
                );
    }
    @Override
    public PerpendicularToLineResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint ruler1point = points.get(this.ruler1point);
        ImagePoint ruler2point = points.get(this.ruler2point);
        ImagePoint startPoint = points.get(this.startPointLine);
        ImagePoint endPoint = points.get(this.EndPointLine);
        ImagePoint perpendicularPoint = points.get(this.PerpendicularToPointLine);

        double distance  = MathUtils.perpendicularDistance(startPoint.getPointX(),startPoint.getPointY(),
                endPoint.getPointX(),endPoint.getPointY(),perpendicularPoint.getPointX(),perpendicularPoint.getPointY());

        double a1 = ruler1point.getPointX();
        double b1 = ruler1point.getPointY();
        double a2 = ruler2point.getPointX();
        double b2 = ruler2point.getPointY();

        double ruler_distance = MathUtils.distance(a1,b1,a2,b2);
        distance = distance * DrawingController.rulerSliderValue / ruler_distance;

        distance = Math.floor(distance * 10)/10;

        return new PerpendicularToLineResult(ruler1point,ruler2point ,startPoint, endPoint, perpendicularPoint, distance);
    }

}
