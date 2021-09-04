package main.java.analysis.utils;

import main.java.database.entities.ImagePoint;


//TODO: handle edge conditions.(division with 0 etc)
public class MathUtils {
    public static double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public static double angle(double centerX, double centerY, double x1, double y1, double x2, double y2){
        double p1c = Math.sqrt(Math.pow(centerX-x1,2)+
                Math.pow(centerY-y1,2));
        double p2c = Math.sqrt(Math.pow(centerX-x2,2)+
                Math.pow(centerY-y2,2));
        double p1p2 = Math.sqrt(Math.pow(x2-x1,2)+
                Math.pow(y2-y1,2));
        return Math.acos((p2c*p2c+p1c*p1c-p1p2*p1p2)/(2*p2c*p1c));
    }

    public static double angle(ImagePoint firstVectorStartPoint, ImagePoint firstVectorEndPoint, ImagePoint secondVectorStartPoint, ImagePoint secondVectorEndPoint){
        double firstVectorX = firstVectorEndPoint.getPointX() - firstVectorStartPoint.getPointX();
        double firstVectorY = firstVectorEndPoint.getPointY() - firstVectorStartPoint.getPointY();
        double secondVectorX = secondVectorEndPoint.getPointX() - secondVectorStartPoint.getPointX();
        double secondVectorY = secondVectorEndPoint.getPointY() - secondVectorStartPoint.getPointY();

        double dotProduct = firstVectorX*secondVectorX + firstVectorY*secondVectorY;
        double length = Math.sqrt((firstVectorX*firstVectorX+firstVectorY*firstVectorY)*
                (secondVectorX*secondVectorX+secondVectorY*secondVectorY));
        return Math.acos(dotProduct/length);
    }
}
