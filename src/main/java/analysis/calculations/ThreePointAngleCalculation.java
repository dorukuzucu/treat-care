package main.java.analysis.calculations;

import main.java.analysis.results.FourPointAngleResult;
import main.java.analysis.results.IResult;
import main.java.analysis.results.ThreePointAngleResult;
import main.java.database.entities.ImagePoint;
import main.java.analysis.utils.MathUtils;
import main.java.utils.PointEnum;

import java.util.HashMap;


public class ThreePointAngleCalculation implements ICalculation {
    PointEnum startPointType;
    PointEnum centerPointType;
    PointEnum endPointType;

    public ThreePointAngleCalculation(PointEnum startPointType, PointEnum centerPointType, PointEnum endPointType){
        this.startPointType = startPointType;
        this.centerPointType = centerPointType;
        this.endPointType = endPointType;

    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.startPointType) &&
                points.containsKey(this.centerPointType) &&
                points.containsKey(this.endPointType));
    }

    @Override
    public IResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint startPoint = points.get(this.startPointType);
        ImagePoint centerPoint = points.get(this.centerPointType);
        ImagePoint endPoint = points.get(this.endPointType);

        double angle =  MathUtils.angle(centerPoint.getPointX(), centerPoint.getPointY(),
                startPoint.getPointX(), startPoint.getPointY(),
                endPoint.getPointX(), endPoint.getPointY());

        return new ThreePointAngleResult(startPoint, centerPoint, endPoint, angle);
    }

}
