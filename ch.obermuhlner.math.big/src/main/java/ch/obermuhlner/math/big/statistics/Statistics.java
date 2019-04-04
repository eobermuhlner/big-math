package ch.obermuhlner.math.big.statistics;

import ch.obermuhlner.math.big.statistics.multivariate.list.MultivariateStreamAsListCalculator;
import ch.obermuhlner.math.big.statistics.multivariate.list.MultivariateStreamAsListsCalculator;
import ch.obermuhlner.math.big.statistics.multivariate.stream.CorrelationCalculator;
import ch.obermuhlner.math.big.statistics.univariate.Histogram;
import ch.obermuhlner.math.big.statistics.univariate.list.MedianCalculator;
//import ch.obermuhlner.math.big.statistics.univariate.list.PopulationSkewnessKurtosisCalculator;
import ch.obermuhlner.math.big.statistics.univariate.list.UnivariateStreamAsListCalculator;
import ch.obermuhlner.math.big.statistics.univariate.stream.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics {
    public static BigDecimal min(List<BigDecimal> values) {
        return new UnivariateStreamAsListCalculator<>(new MinCalculator()).getResult(values);
    }

    public static BigDecimal max(List<BigDecimal> values) {
        return new UnivariateStreamAsListCalculator<>(new MaxCalculator()).getResult(values);
    }

    public static BigDecimal sum(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new SumCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal product(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new ProductCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal arithmeticMean(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new ArithmeticMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal geometricMean(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new GeometricMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal harmonicMean(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new HarmonicMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal medianSorted(List<BigDecimal> sortedValues, MathContext mathContext) {
        return new MedianCalculator(mathContext).getResult(sortedValues);
    }

    public static BigDecimal medianUnsorted(List<BigDecimal> unsortedValues, MathContext mathContext) {
        List<BigDecimal> sortedValues = new ArrayList<>(unsortedValues);
        Collections.sort(sortedValues);
        return new MedianCalculator(mathContext).getResult(sortedValues);
    }

    public static BigDecimal populationVariance(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new PopulationVarianceCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal sampleVariance(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new SampleVarianceCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal populationStandardDeviation(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new PopulationStandardDeviationCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal sampleStandardDeviation(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new SampleStandardDeviationCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal populationSkewness(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext)).getResult(values).skewness;
        //return new PopulationSkewnessKurtosisCalculator(mathContext).getResult(values);
    }

    public static BigDecimal populationKurtosis(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext)).getResult(values).kurtosis;
    }

    public static SkewnessKurtosis populationSkewnessKurtosis(List<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsListCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext)).getResult(values);
    }

    public static Histogram histogram(List<BigDecimal> values, BigDecimal start, BigDecimal end, BigDecimal step) {
        return new UnivariateStreamAsListCalculator<>(new HistogramCalculator(start, end, step)).getResult(values);
    }

    public static BigDecimal correlation(List<BigDecimal[]> xyValues, MathContext mathContext) {
        return new MultivariateStreamAsListCalculator<>(new CorrelationCalculator(mathContext)).getResult(xyValues);
    }

    public static BigDecimal correlation(List<BigDecimal> xValues, List<BigDecimal> yValues, MathContext mathContext) {
        return new MultivariateStreamAsListsCalculator<>(new CorrelationCalculator(mathContext)).getResult(xValues, yValues);
    }
}
