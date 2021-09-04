package main.java.analysis.sets;

import main.java.analysis.calculations.FourPointAngleCalculation;
import main.java.analysis.calculations.ThreePointAngleCalculation;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.utils.PointEnum;


public class SteinerAnalysis extends BaseAnalysis {
    public SteinerAnalysis(){
        this.calculations.put(CalculationTypeEnum.SNA, new ThreePointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.SNB, new ThreePointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.B_POINT));
        this.calculations.put(CalculationTypeEnum.SN_GO_GN, new FourPointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.GONION, PointEnum.GNATHION));
    }
}
