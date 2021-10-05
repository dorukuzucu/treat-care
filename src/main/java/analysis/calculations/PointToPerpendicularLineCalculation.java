package main.java.analysis.calculations;

import main.java.analysis.results.IResult;
import main.java.analysis.results.PointToPerpendicularLineResults;
import main.java.analysis.results.ThreePointAngleResult;
import main.java.analysis.utils.MathUtils;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public class PointToPerpendicularLineCalculation implements ICalculation {

    private PointEnum ruler1pointtype;
    private PointEnum ruler2pointtype;
    private PointEnum startPointtype;
    private PointEnum endPointtype;
    private PointEnum perpendicular_pointtype;
    private PointEnum molar_distal_pointtype;

    public PointToPerpendicularLineCalculation(PointEnum ruler1pointtype,PointEnum ruler2pointtype,PointEnum startPointtype,
                                               PointEnum endPointtype,
                                               PointEnum perpendicular_pointtype,PointEnum molar_distal_pointtype){
        this.ruler1pointtype = ruler1pointtype;
        this.ruler2pointtype = ruler2pointtype;
        this.startPointtype = startPointtype;
        this.endPointtype = endPointtype;
        this.perpendicular_pointtype = perpendicular_pointtype;
        this.molar_distal_pointtype = molar_distal_pointtype;

    }

    @Override
    public boolean isAvailable(HashMap<PointEnum, ImagePoint> points) {
        return (points.containsKey(this.ruler1pointtype) &&
                points.containsKey(this.ruler2pointtype) &&
                points.containsKey(this.startPointtype) &&
                points.containsKey(this.endPointtype) &&
                points.containsKey(this.perpendicular_pointtype) &&
                points.containsKey(this.molar_distal_pointtype));
    }

    @Override
    public IResult calculate(HashMap<PointEnum, ImagePoint> points) {
        if(!this.isAvailable(points)){
            return null;
        }
        ImagePoint ruler1 = points.get(this.ruler1pointtype);
        ImagePoint ruler2 = points.get(this.ruler2pointtype);
        ImagePoint startPoint = points.get(this.startPointtype);
        ImagePoint endPoint = points.get(this.endPointtype);
        ImagePoint perpendicular_point = points.get(this.perpendicular_pointtype);
        ImagePoint molar_distal_point = points.get(this.molar_distal_pointtype);


        double x1;
        double y1;
        double x2;
        double y2;
        double x3;
        double y3;

        double xIntercept;
        double yIntercept;

        x1 = startPoint.getPointX();
        y1 = startPoint.getPointY();

        x2= endPoint.getPointX();
        y2 = endPoint.getPointY();

        x3= perpendicular_point.getPointX();
        y3 = perpendicular_point.getPointY();

       double k = ((y2-y1) * (x3-x1) - (x2-x1) * (y3-y1)) / ((y2-y1) * (y2 -y1) + (x2-x1) * (x2-x1));
        xIntercept = x3 - k * (y2-y1);
        yIntercept = y3 + k * (x2-x1);

        double a1 = ruler1.getPointX();
        double b1 = ruler1.getPointY();
        double a2 = ruler2.getPointX();
        double b2 = ruler2.getPointY();

        double ruler_distance = MathUtils.distance(a1,b1,a2,b2);
        double distance  = MathUtils.perpendicularDistance(x3,y3,
                xIntercept,yIntercept,molar_distal_point.getPointX(),molar_distal_point.getPointY());

        distance = distance * DrawingController.rulerSliderValue / ruler_distance;
        distance = Math.floor(distance * 10)/10;

        return new PointToPerpendicularLineResults(ruler1,ruler2,startPoint,endPoint, perpendicular_point, molar_distal_point,distance);
    }

}
