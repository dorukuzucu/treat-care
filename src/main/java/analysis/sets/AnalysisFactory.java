package main.java.analysis.sets;

import main.java.analysis.utils.AnalysisEnum;

public class AnalysisFactory {
    public BaseAnalysis getAnalysis(AnalysisEnum analysisType){
        if(analysisType==AnalysisEnum.STEINER){
            return new SteinerAnalysis();
        } else if(analysisType==AnalysisEnum.RICKETS){
            return new RicketsAnalysis();
        }
        return null;
    }
}
