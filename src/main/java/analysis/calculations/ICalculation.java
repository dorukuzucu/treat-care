package main.java.analysis.calculations;

import main.java.analysis.results.IResult;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public interface ICalculation {
    boolean isAvailable(HashMap<PointEnum, ImagePoint> points);
    IResult calculate(HashMap<PointEnum, ImagePoint> points);
}
