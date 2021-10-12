package main.java.analysis.sets;

import main.java.analysis.calculations.ICalculation;
import main.java.analysis.results.IResult;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.HashMap;

public interface IAnalysisSet {

    public void setPoints(HashMap<PointEnum, ImagePoint> points);
    public void execute();
    public HashMap<CalculationTypeEnum, IResult> getResult();
}
