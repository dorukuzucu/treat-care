package main.java.analysis.calculations;

import main.java.analysis.results.FourPointAngleResult;
import main.java.analysis.results.LineResult;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.analysis.utils.MathUtils;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class LineCalculation implements ICalculation {

    PointEnum ruler1type;
    PointEnum ruler2type;
    PointEnum startPointType;
    PointEnum endPointType;



    public LineCalculation(PointEnum ruler1type , PointEnum ruler2type,PointEnum startPointType, PointEnum endPointType){
            this.ruler1type = ruler1type;
            this.ruler2type = ruler2type;
            this.startPointType = startPointType;
            this.endPointType = endPointType;
    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.startPointType) && points.containsKey(this.endPointType) &&points.containsKey(this.ruler1type) &&points.containsKey(this.ruler2type));
    }

    @Override
    public LineResult  calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint ruler1 = points.get(this.ruler1type);
        ImagePoint ruler2 = points.get(this.ruler2type);
        ImagePoint startPoint = points.get(this.startPointType);
        ImagePoint endPoint = points.get(this.endPointType);

        double a1 = ruler1.getPointX();
        double b1 = ruler1.getPointY();
        double a2 = ruler2.getPointX();
        double b2 = ruler2.getPointY();

        double x1 = startPoint.getPointX();
        double y1 = startPoint.getPointY();
        double x2 = endPoint.getPointX();
        double y2 = endPoint.getPointY();

        double distance = MathUtils.distance(x1, y1, x2, y2);
        double ruler_distance = MathUtils.distance(a1,b1,a2,b2);
        distance = distance * DrawingController.rulerSliderValue / ruler_distance;

        distance = Math.floor(distance * 10)/10;
        return new LineResult(ruler1,ruler2,startPoint, endPoint, distance);
    }
}
