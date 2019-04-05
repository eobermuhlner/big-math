package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.statistics.univariate.list.UnivariateStreamAsListCalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static ch.obermuhlner.math.big.example.internal.MicroBenchmark.performanceReportOverLambda;

public class PerformanceStatistics {
    private static final String OUTPUT_DIRECTORY = "docu/benchmarks/";
    private static final int REPEATS = 100;
    private static final int WARMUP_REPEATS = 10;

    public static void main(String[] args) {
        performanceReport_statistics();
    }

    private static void performanceReport_statistics() {
        performanceReport_means();
        performanceReport_skewness_kurtosis();
    }

    private static void performanceReport_means() {
        MathContext mc = new MathContext(64);

        MicroBenchmark.performanceReportOverLambda(
                OUTPUT_DIRECTORY,
                "perf_statistics_means.csv",
                10,
                1000,
                10,
                REPEATS,
                WARMUP_REPEATS,
                (i) -> createValues(i),
                Arrays.asList("list.arithmeticMean", "stream.arithmeticMean"),
                (values) -> new ch.obermuhlner.math.big.statistics.univariate.list.ArithmeticMeanCalculator(mc).getResult(values),
                (values) -> new UnivariateStreamAsListCalculator(new ch.obermuhlner.math.big.statistics.univariate.stream.ArithmeticMeanCalculator(mc)).getResult(values));
    }

    private static void performanceReport_skewness_kurtosis() {
        MathContext mc = new MathContext(64);

        MicroBenchmark.performanceReportOverLambda(
                OUTPUT_DIRECTORY,
                "perf_statistics_skewness.csv",
                0, 1000,
                10,
                REPEATS,
                WARMUP_REPEATS,
                (i) -> createValues(i + 10),
                Arrays.asList("list.skewness", "stream.skewness"),
                (values) -> new ch.obermuhlner.math.big.statistics.univariate.list.PopulationSkewnessCalculator(mc).getResult(values),
                (values) -> new UnivariateStreamAsListCalculator(new ch.obermuhlner.math.big.statistics.univariate.stream.PopulationSkewnessKurtosisCalculator(mc, true, false)).getResult(values));


        MicroBenchmark.performanceReportOverLambda(
                OUTPUT_DIRECTORY,
                "perf_statistics_kurtosis.csv",
                0, 1000,
                10,
                REPEATS,
                WARMUP_REPEATS,
                (i) -> createValues(i + 10),
                Arrays.asList("list.kurtosis", "stream.kurtosis"),
                (values) -> new ch.obermuhlner.math.big.statistics.univariate.list.PopulationKurtosisCalculator(mc).getResult(values),
                (values) -> new UnivariateStreamAsListCalculator(new ch.obermuhlner.math.big.statistics.univariate.stream.PopulationSkewnessKurtosisCalculator(mc, false, true)).getResult(values));
    }

    private static List<BigDecimal> createValues(int count) {
        Random random = new Random(1);

        List<BigDecimal> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            result.add(BigDecimal.valueOf(random.nextDouble()));
        }

        return result;
    }
}
