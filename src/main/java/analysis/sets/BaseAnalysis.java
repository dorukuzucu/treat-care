package main.java.analysis.sets;

import main.java.analysis.calculations.ICalculation;
import main.java.analysis.results.IResult;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.database.entities.ImagePoint;
import main.java.utils.PointEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class BaseAnalysis implements IAnalysisSet{

    HashMap<PointEnum, ImagePoint> points = null;

    @Override
    public void setPoints(HashMap<PointEnum, ImagePoint> points){
        this.points = points;
    }

    @Override
    public void execute() {
        if(this.points==null){
            return;
        }
        for(Map.Entry<CalculationTypeEnum, ICalculation> calculation: this.calculations.entrySet()){
            CalculationTypeEnum calculationType = calculation.getKey();
            var result = calculation.getValue().calculate(this.points);
            this.results.put(calculationType, result);
        }
    }

    @Override
    public HashMap<CalculationTypeEnum, IResult> getResult() {
        return this.results;
    }
}
