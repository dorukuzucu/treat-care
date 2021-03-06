package main.java.analysis.sets;

import main.java.analysis.calculations.*;

import main.java.analysis.results.IResult;
import main.java.analysis.utils.CalculationTypeEnum;
import main.java.utils.PointEnum;

import java.util.HashMap;


public class RicketsAnalysis extends BaseAnalysis {
    public RicketsAnalysis(){
        super();

        this.calculations.put(CalculationTypeEnum.FACIAL_DEPTH, new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.NASION, PointEnum.POG));
        this.calculations.put(CalculationTypeEnum.FACIAL_AXIS, new FourPointAngleCalculation(PointEnum.BASION, PointEnum.NASION, PointEnum.PT, PointEnum.GNATHION));
        this.calculations.put(CalculationTypeEnum.FACIAL_TAPER, new FourPointAngleCalculation(PointEnum.GONION, PointEnum.GNATHION, PointEnum.NASION, PointEnum.POG));
        this.calculations.put(CalculationTypeEnum.MANDIBULAR_PLANE, new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.GONION, PointEnum.MENTON));
        this.calculations.put(CalculationTypeEnum.OVERBITE, new RelativeLineCalculation(PointEnum.RULER_1, PointEnum.RULER_2,PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_CROWN_TIP,true));
        this.calculations.put(CalculationTypeEnum.OVERJET, new RelativeLineCalculation(PointEnum.RULER_1, PointEnum.RULER_2,PointEnum.UPPER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_CROWN_TIP,false));
        this.calculations.put(CalculationTypeEnum.MAXILLARY_DEPTH, new FourPointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.NASION, PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.CRANIAL_DEFLECTION_ANGLE, new FourPointAngleCalculation(PointEnum.BASION, PointEnum.NASION, PointEnum.PORION, PointEnum.ORBITALE));
        this.calculations.put(CalculationTypeEnum.ANTERIOR_CRANIAL_BASE, new IntersectionToDistanceCalculation(PointEnum.RULER_1, PointEnum.RULER_2, PointEnum.BASION, PointEnum.NASION,PointEnum.PT,PointEnum.GNATHION,PointEnum.NASION));
        this.calculations.put(CalculationTypeEnum.CORPUS_LENGHT,new XiPointRelatedCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.SIGMOID_NOTCH,PointEnum.MOST_CONVEX_POINT_OF_RAMUS,PointEnum.MOST_EXTERIOR_POINT_OF_RAMUS, PointEnum.MOST_BOTTOM_POINT_OF_RAMUS , PointEnum.DC_POINT,PointEnum.PM_POINT,false,PointEnum.PM_POINT));
        this.calculations.put(CalculationTypeEnum.MOLAR_RELATION, new RelativeLineCalculation(PointEnum.RULER_1, PointEnum.RULER_2, PointEnum.UPPER_MOLAR_DISTAL_SURFACE, PointEnum.LOWER_MOLAR_DISTAL_SURFACE,false));
        this.calculations.put(CalculationTypeEnum.PTV_U6, new PointToPerpendicularLineCalculation(PointEnum.RULER_1, PointEnum.RULER_2, PointEnum.ORBITALE, PointEnum.PORION, PointEnum.PT, PointEnum.UPPER_MOLAR_DISTAL_SURFACE));
        this.calculations.put(CalculationTypeEnum.MAXILARY_HEIGHT, new VerticalLineToThreePointAngleCalculation(PointEnum.PORION, PointEnum.ORBITALE, PointEnum.PT, PointEnum.NASION, PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.L1_NA,new FourPointAngleCalculation(PointEnum.A_POINT, PointEnum.POG, PointEnum.LOWER_INCISOR_CROWN_TIP, PointEnum.LOWER_INCISOR_ROOT_TIP));
        this.calculations.put(CalculationTypeEnum.L1_NA_DISTANCE,new PerpendicularToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.A_POINT, PointEnum.POG , PointEnum.LOWER_INCISOR_CROWN_TIP));
        this.calculations.put(CalculationTypeEnum.CONVEXITY,new PerpendicularToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.NASION, PointEnum.POG , PointEnum.A_POINT));
        this.calculations.put(CalculationTypeEnum.MANDIBULAR_ARC,new XiPointRelatedCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.SIGMOID_NOTCH,PointEnum.MOST_CONVEX_POINT_OF_RAMUS,PointEnum.MOST_EXTERIOR_POINT_OF_RAMUS, PointEnum.MOST_BOTTOM_POINT_OF_RAMUS , PointEnum.DC_POINT,PointEnum.PM_POINT,true,PointEnum.PM_POINT));
        this.calculations.put(CalculationTypeEnum.LOWER_LIP_PROTRUSION,new HorizontalPointToLineCalculation(PointEnum.RULER_1,PointEnum.RULER_2,PointEnum.TIP_OF_NOSE, PointEnum.ST_POGONION , PointEnum.LOWER_LIP));
    }
}
