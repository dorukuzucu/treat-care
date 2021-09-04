package main.java.analysis.results;

abstract class BaseResult implements IResult{
    double result;

    public double getResult() {
        return this.result;
    }

    @Override
    public String toString() {
        return Double.toString(this.result);
    }
}
