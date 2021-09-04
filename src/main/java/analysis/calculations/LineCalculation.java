package main.java.analysis.calculations;

import main.java.analysis.results.LineResult;
import main.java.database.entities.ImagePoint;
import main.java.analysis.utils.MathUtils;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class LineCalculation implements ICalculation {

    PointEnum startPointType;
    PointEnum endPointType;

    public LineCalculation(PointEnum startPointType, PointEnum endPointType){
            this.startPointType = startPointType;
            this.endPointType = endPointType;
    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.startPointType) && points.containsKey(this.endPointType));
    }

    @Override
    public LineResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint startPoint = points.get(this.startPointType);
        ImagePoint endPoint = points.get(this.endPointType);

        double x1 = startPoint.getPointX();
        double y1 = startPoint.getPointY();
        double x2 = endPoint.getPointX();
        double y2 = endPoint.getPointY();
        double distance = MathUtils.distance(x1, y1, x2, y2);

        return new LineResult(startPoint, endPoint, distance);
    }
}
