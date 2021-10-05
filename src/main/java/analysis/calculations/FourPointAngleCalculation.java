package main.java.analysis.calculations;

import main.java.analysis.results.FourPointAngleResult;
import main.java.database.entities.ImagePoint;
import main.java.analysis.utils.MathUtils;
import main.java.utils.PointEnum;


import java.util.HashMap;

public class FourPointAngleCalculation implements ICalculation {


    private PointEnum firstVectorStartPointType;
    private PointEnum firstVectorEndPointType;
    private PointEnum secondVectorStartPointType;
    private PointEnum secondVectorEndPointType;

    public FourPointAngleCalculation(PointEnum firstVectorStartPointType, PointEnum firstVectorEndPointType,
                                     PointEnum secondVectorStartPointType, PointEnum secondVectorEndPointType){
        this.firstVectorStartPointType = firstVectorStartPointType;
        this.firstVectorEndPointType = firstVectorEndPointType;
        this.secondVectorStartPointType = secondVectorStartPointType;
        this.secondVectorEndPointType = secondVectorEndPointType;
    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.firstVectorStartPointType) &&
                points.containsKey(this.firstVectorEndPointType) &&
                points.containsKey(this.secondVectorStartPointType) &&
                points.containsKey(this.secondVectorEndPointType));
    }

    @Override
    public FourPointAngleResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint firstVectorStartPoint = points.get(this.firstVectorStartPointType);
        ImagePoint firstVectorEndPoint = points.get(this.firstVectorEndPointType);
        ImagePoint secondVectorStartPoint = points.get(this.secondVectorStartPointType);
        ImagePoint secondVectorEndPoint = points.get(this.secondVectorEndPointType);

        double angle = MathUtils.angle(firstVectorStartPoint,  firstVectorEndPoint,
                secondVectorStartPoint,  secondVectorEndPoint);

        return new FourPointAngleResult(firstVectorStartPoint,  firstVectorEndPoint,
                secondVectorStartPoint,  secondVectorEndPoint, angle);
    }
}
