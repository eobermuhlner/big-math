package ch.obermuhlner.math.big.statistics;

import ch.obermuhlner.math.big.statistics.multivariate.collection.MultivariateStreamAsCollectionCalculator;
import ch.obermuhlner.math.big.statistics.multivariate.collection.MultivariateStreamAsCollectionsCalculator;
import ch.obermuhlner.math.big.statistics.multivariate.stream.CorrelationCalculator;
import ch.obermuhlner.math.big.statistics.type.Histogram;
import ch.obermuhlner.math.big.statistics.type.SkewnessKurtosis;
import ch.obermuhlner.math.big.statistics.univariate.collection.MedianCalculator;
import ch.obermuhlner.math.big.statistics.univariate.collection.UnivariateStreamAsCollectionCalculator;
import ch.obermuhlner.math.big.statistics.univariate.stream.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Statistics {
    public static BigDecimal min(Collection<BigDecimal> values) {
        return new UnivariateStreamAsCollectionCalculator<>(new MinCalculator()).getResult(values);
    }

    public static BigDecimal max(Collection<BigDecimal> values) {
        return new UnivariateStreamAsCollectionCalculator<>(new MaxCalculator()).getResult(values);
    }

    public static BigDecimal sum(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SumCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal product(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new ProductCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal arithmeticMean(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new ArithmeticMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal geometricMean(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new GeometricMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal harmonicMean(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new HarmonicMeanCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal medianSorted(List<BigDecimal> sortedValues, MathContext mathContext) {
        return new MedianCalculator(mathContext).getResult(sortedValues);
    }

    public static BigDecimal medianUnsorted(Collection<BigDecimal> unsortedValues, MathContext mathContext) {
        List<BigDecimal> sortedValues = new ArrayList<>(unsortedValues);
        Collections.sort(sortedValues);
        return new MedianCalculator(mathContext).getResult(sortedValues);
    }

    public static BigDecimal populationVariance(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationVarianceCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal sampleVariance(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleVarianceCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal populationStandardDeviation(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationStandardDeviationCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal sampleStandardDeviation(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleStandardDeviationCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal populationSkewness(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext, true, false)).getResult(values).skewness;
    }

    public static BigDecimal populationKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext, false, true)).getResult(values).kurtosis;
    }

    public static BigDecimal populationExcessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationSkewnessExcessKurtosisCalculator(mathContext, false, true)).getResult(values).kurtosis;
    }

    public static SkewnessKurtosis populationSkewnessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationSkewnessKurtosisCalculator(mathContext)).getResult(values);
    }

    public static SkewnessKurtosis populationSkewnessExcessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new PopulationSkewnessExcessKurtosisCalculator(mathContext)).getResult(values);
    }

    public static BigDecimal sampleSkewness(Collection<BigDecimal> values, MathContext mathContext) {
        //return new UnivariateStreamAsCollectionCalculator<>(new SampleSkewnessKurtosisCalculator(mathContext, true, false)).getResult(values).skewness;
        return new ch.obermuhlner.math.big.statistics.univariate.collection.SampleSkewnessCalculator(mathContext).getResult(values);
    }

    public static BigDecimal sampleKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleSkewnessKurtosisCalculator(mathContext, false, true)).getResult(values).kurtosis;
    }

    public static BigDecimal sampleExcessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleSkewnessExcessKurtosisCalculator(mathContext, false, true)).getResult(values).kurtosis;
    }

    public static SkewnessKurtosis sampleSkewnessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleSkewnessKurtosisCalculator(mathContext)).getResult(values);
    }

    public static SkewnessKurtosis sampleSkewnessExcessKurtosis(Collection<BigDecimal> values, MathContext mathContext) {
        return new UnivariateStreamAsCollectionCalculator<>(new SampleSkewnessExcessKurtosisCalculator(mathContext)).getResult(values);
    }

    public static Histogram histogram(Collection<BigDecimal> values, BigDecimal start, BigDecimal end, BigDecimal step) {
        return new UnivariateStreamAsCollectionCalculator<>(new HistogramCalculator(start, end, step)).getResult(values);
    }

    public static BigDecimal correlation(Collection<BigDecimal[]> xyValues, MathContext mathContext) {
        return new MultivariateStreamAsCollectionCalculator<>(new CorrelationCalculator(mathContext)).getResult(xyValues);
    }

    public static BigDecimal correlation(List<BigDecimal> xValues, List<BigDecimal> yValues, MathContext mathContext) {
        return new MultivariateStreamAsCollectionsCalculator<>(new CorrelationCalculator(mathContext)).getResult(xValues, yValues);
    }
}
