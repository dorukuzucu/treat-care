package main.java.analysis.utils;

import main.java.analysis.calculations.LineCalculation;
import main.java.analysis.calculations.PerpendicularToLineCalculation;
import main.java.analysis.results.PerpendicularToLineResult;
import main.java.controllers.DrawingController;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.rmi.MarshalException;
import java.text.DecimalFormat;
import java.text.NumberFormat;


//TODO: handle edge conditions.(division with 0 etc)
public class MathUtils {


     public static double perpendicularDistance(double x1,double y1,double x2,double y2,double x3,double y3)
    {
        double px=x2-x1;
        double py=y2-y1;
        double temp=(px*px)+(py*py);
        double u=((x3 - x1) * px + (y3 - y1) * py) / (temp);

        double x = x1 + u * px;
        double y = y1 + u * py;

        double dx = x - x3;
        double dy = y - y3;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;

    }

    public static double distance (double x1, double y1, double x2, double y2){
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static double angle(double centerX, double centerY, double x1, double y1, double x2, double y2){
        double p1c = Math.sqrt(Math.pow(centerX-x1,2)+
                Math.pow(centerY-y1,2));
        double p2c = Math.sqrt(Math.pow(centerX-x2,2)+
                Math.pow(centerY-y2,2));
        double p1p2 = Math.sqrt(Math.pow(x2-x1,2)+
                Math.pow(y2-y1,2));

        double oneDecimal = Math.floor(Math.acos((p2c*p2c+p1c*p1c-p1p2*p1p2)/(2*p2c*p1c)) * 57.3*10) /10;
        return oneDecimal;
    }

    //TODO remove ImagePoint cohesion
    public static double angle(ImagePoint firstVectorStartPoint, ImagePoint firstVectorEndPoint, ImagePoint secondVectorStartPoint, ImagePoint secondVectorEndPoint){
        double firstVectorX = firstVectorEndPoint.getPointX() - firstVectorStartPoint.getPointX();
        double firstVectorY = firstVectorEndPoint.getPointY() - firstVectorStartPoint.getPointY();
        double secondVectorX = secondVectorEndPoint.getPointX() - secondVectorStartPoint.getPointX();
        double secondVectorY = secondVectorEndPoint.getPointY() - secondVectorStartPoint.getPointY();

        double dotProduct = firstVectorX*secondVectorX + firstVectorY*secondVectorY;
        double length = Math.sqrt((firstVectorX*firstVectorX+firstVectorY*firstVectorY)*
                (secondVectorX*secondVectorX+secondVectorY*secondVectorY));
        double oneDecimal = Math.floor(Math.acos(dotProduct/length) * 57.3 * 10)/10;
        return oneDecimal;
    }


}
