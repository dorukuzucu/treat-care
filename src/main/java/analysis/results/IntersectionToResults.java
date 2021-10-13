package main.java.analysis.results;

import main.java.database.entities.ImagePoint;

public class IntersectionToResults extends BaseResult{

    private ImagePoint ruler1point;
    private ImagePoint ruler2point;
    private ImagePoint firstPoint;
    private ImagePoint secondPoint;
    private ImagePoint thirdPoint;
    private ImagePoint fourthPoint;
    private ImagePoint distancePoint;




    public IntersectionToResults(ImagePoint ruler1point,
                                 ImagePoint ruler2point,
                                 ImagePoint firstPoint,
                                 ImagePoint secondPoint,
                                 ImagePoint thirdPoint,
                                 ImagePoint fourthPoint,
                                 ImagePoint distancePoint,
                                 double length){

        this.ruler1point = ruler1point;
        this.ruler2point = ruler2point;
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.thirdPoint = thirdPoint;
        this.fourthPoint = fourthPoint;
        this.distancePoint = distancePoint;
        this.result = length;
    }

    public double getResult() {
        return result;
    }

    public ImagePoint getStartPoint() {
        return firstPoint;
    }

    public ImagePoint getEndPoint() {
        return secondPoint;
    }

}

