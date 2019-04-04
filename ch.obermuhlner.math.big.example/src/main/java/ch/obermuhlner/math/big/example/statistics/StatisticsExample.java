package ch.obermuhlner.math.big.example.statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.obermuhlner.math.big.statistics.Statistics;
import ch.obermuhlner.math.big.statistics.StatisticsCollectors;
import ch.obermuhlner.math.big.statistics.univariate.Histogram;
import ch.obermuhlner.math.big.statistics.univariate.stream.ArithmeticMeanCalculator;

public class StatisticsExample {
    private static final MathContext MC = MathContext.DECIMAL32;

    public static void main(String[] args) {
        printExampleUnivariateStatistics();
        printExampleMultivariateStatistics();

        printExampleStreamStatistics();
    }

    private static void printExampleUnivariateStatistics() {
        System.out.println("=== Univariate Statistics");

        List<BigDecimal> values = toBigDecimals(1, 2, 3, 4, 4, 4, 5, 5, 5, 5, 5, 9);
        Collections.sort(values);

        Histogram histogram = Statistics.histogram(values, BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(1));

        System.out.println("Values:          " + values);
        System.out.println("Arithmetic mean: " + Statistics.arithmeticMean(values, MC));
        System.out.println("Geometric mean:  " + Statistics.geometricMean(values, MC));
        System.out.println("Median:          " + Statistics.medianSorted(values, MC));
        System.out.println("Min:             " + Statistics.min(values));
        System.out.println("Max:             " + Statistics.max(values));
        System.out.println("Pop    StdDev:   " + Statistics.populationStandardDeviation(values, MC));
        System.out.println("Sample StdDev:   " + Statistics.sampleStandardDeviation(values, MC));
        System.out.println("Pop    Var:      " + Statistics.populationVariance(values, MC));
        System.out.println("Sample Var:      " + Statistics.sampleVariance(values, MC));
        System.out.println("Pop    Skewness: " + Statistics.populationSkewness(values, MC));
        System.out.println("Sample Skewness: " + Statistics.sampleSkewness(values, MC));
        System.out.println("Pop    Kurtosis: " + Statistics.populationKurtosis(values, MC));
        System.out.println("Sample Kurtosis: " + Statistics.sampleKurtosis(values, MC));
        System.out.println("Pop    Excess Kurtosis: " + Statistics.populationExcessKurtosis(values, MC));
        System.out.println("Sample Excess Kurtosis: " + Statistics.sampleExcessKurtosis(values, MC));
        System.out.println("Histogram:       " + histogram);

        printHistogram(histogram);
    }

    private static void printExampleMultivariateStatistics() {
        System.out.println("=== Multivariate Statistics");

        List<BigDecimal> xValues = toBigDecimals(1, 2, 3, 4, 4, 4, 5, 5, 5, 5, 5, 9);
        List<BigDecimal> yValues = toBigDecimals(1, 3, 2, 4, 5, 3, 5, 6, 4, 5, 5, 8);

        System.out.println("X Values:        " + xValues);
        System.out.println("Y Values:        " + yValues);
        System.out.println("Correlation:     " + Statistics.correlation(xValues, yValues, MC));
    }

    private static void printExampleStreamStatistics() {
        System.out.println("=== Stream Statistics");

        BigDecimal arithmeticMean = Stream
                .of(1, 2, 3, 4, 4, 4, 5, 5, 5, 5, 5, 9)
                .map(v -> BigDecimal.valueOf(v))
                .collect(StatisticsCollectors.arithmeticMean(MC));
        System.out.println("Arithmetic mean: " + arithmeticMean);

        ArithmeticMeanCalculator arithmeticMeanCalculator = new ArithmeticMeanCalculator(MC);
        List<BigDecimal> runningArithmeticMean = Stream
                .of(1, 2, 3, 4, 4, 4, 5, 5, 5, 5, 5, 9)
                .map(v -> BigDecimal.valueOf(v))
                .map(v -> {
                    arithmeticMeanCalculator.add(v);
                    return arithmeticMeanCalculator.getResult();
                })
                .collect(Collectors.toList());
        System.out.println("Running Arithmetic mean: " + runningArithmeticMean);

    }

    private static void printHistogram(Histogram histogram) {
        for (int i = 0; i < histogram.size(); i++) {
            System.out.printf("%4.1f - %4.1f : %3d |%s\n", histogram.getStart(i), histogram.getEnd(i), histogram.getCount(i), repeat("#", histogram.getCount(i)));
        }
    }

    private static String repeat(String s, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append(s);
        }

        return builder.toString();
    }

    private static List<BigDecimal> toBigDecimals(double... values) {
        return Arrays.stream(values)
                .mapToObj(v -> BigDecimal.valueOf(v))
                .collect(Collectors.toList());
    }
}
