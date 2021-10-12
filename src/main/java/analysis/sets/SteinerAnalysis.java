package main.java.analysis.sets;

import main.java.analysis.calculations.*;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.utils.PointEnum;

import java.util.HashMap;


public class SteinerAnalysis extends BaseAnalysis {
    public SteinerAnalysis(){
        super();

        this.calculations.put(CalculationTypeEnum.SADDLE, new ThreePointAngleCalculation(PointEnum.NASION, PointEnum.SELLA, PointEnum.ARTICULARE));
        this.calculations.put(CalculationTypeEnum.ARTICULAR_ANGLE, new ThreePointAngleCalculation(PointEnum.SELLA, PointEnum.ARTICULARE, PointEnum.GONION));
        this.calculations.put(CalculationTypeEnum.GONIAL_ANGLE, new ThreePointAngleCalculation(PointEnum.ARTICULARE, PointEnum.GONION, PointEnum.MENTON));

        this.calculations.put(CalculationTypeEnum.SN_GO_GN, new FourPointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.GONION, PointEnum.GNATHION));
        this.calculations.put(CalculationTypeEnum.FMA, new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.GONION, PointEnum.MENTON));
        this.calculations.put(CalculationTypeEnum.Y_AXIS_ANGLE, new FourPointAngleCalculation(PointEnum.SELLA, PointEnum.GNATHION, PointEnum.PORION, PointEnum.ORBITALE));

        this.calculations.put(CalculationTypeEnum.SNA, new ThreePointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.SNB, new ThreePointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.B_POINT));
        this.calculations.put(CalculationTypeEnum.ANB, new ThreePointAngleCalculation(PointEnum.A_POINT, PointEnum.NASION, PointEnum.B_POINT));

        this.calculations.put(CalculationTypeEnum.U1_SN,new FourPointAngleCalculation(PointEnum.SELLA, PointEnum.NASION, PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.UPPER_INCISOR_ROOT_TIP));
        this.calculations.put(CalculationTypeEnum.U1_FH,new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.UPPER_INCISOR_ROOT_TIP));
        this.calculations.put(CalculationTypeEnum.U1_NA,new FourPointAngleCalculation(PointEnum.UPPER_INCISOR_ROOT_TIP, PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.NASION, PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.U1_NA_DISTANCE,new PerpendicularToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.NASION, PointEnum.A_POINT , PointEnum.UPPER_INCISOR_CROWN_TIP));

        this.calculations.put(CalculationTypeEnum.IMPA,new FourPointAngleCalculation(PointEnum.GONION, PointEnum.MENTON, PointEnum.LOWER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_ROOT_TIP));
        this.calculations.put(CalculationTypeEnum.FMIA,new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.LOWER_INCISOR_ROOT_TIP, PointEnum.LOWER_INCISOR_CROWN_TIP));
        this.calculations.put(CalculationTypeEnum.L1_NB,new FourPointAngleCalculation(PointEnum.NASION, PointEnum.B_POINT, PointEnum.LOWER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_ROOT_TIP));
        this.calculations.put(CalculationTypeEnum.L1_NB_DISTANCE,new PerpendicularToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.NASION, PointEnum.B_POINT , PointEnum.LOWER_INCISOR_CROWN_TIP));

        this.calculations.put(CalculationTypeEnum.UPPER_LIP_TO_S_LINE,new HorizontalPointToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.MIDPOINT_OF_COLUMELLA, PointEnum.ST_POGONION , PointEnum.UPPER_LIP));
        this.calculations.put(CalculationTypeEnum.LOWER_LIP_TO_S_LINE,new HorizontalPointToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.MIDPOINT_OF_COLUMELLA, PointEnum.ST_POGONION , PointEnum.LOWER_LIP));
    }
}
