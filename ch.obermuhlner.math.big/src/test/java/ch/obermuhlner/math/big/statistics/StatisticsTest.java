package ch.obermuhlner.math.big.statistics;

import ch.obermuhlner.math.big.statistics.univariate.Histogram;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StatisticsTest {

    private static final MathContext MC = MathContext.DECIMAL64;

    @Test
    public void testSum() {
        assertBigDecimal(BigDecimal.valueOf(6), Statistics.sum(toBigDecimals(1, 2, 3), MC));
    }

    @Test
    public void testProduct() {
        assertBigDecimal(BigDecimal.valueOf(24), Statistics.product(toBigDecimals(2, 3, 4), MC));
    }

    @Test
    public void testMin() {
        assertBigDecimal(BigDecimal.valueOf(4), Statistics.min(toBigDecimals(6, 5, 4, 7)));
        assertBigDecimal(BigDecimal.valueOf(-1), Statistics.min(toBigDecimals(3, 2, 1, 0, -1)));
    }

    @Test
    public void testMax() {
        assertBigDecimal(BigDecimal.valueOf(7), Statistics.max(toBigDecimals(6, 5, 4, 7)));
        assertBigDecimal(BigDecimal.valueOf(3), Statistics.max(toBigDecimals(3, 2, 1, 0, -1)));
    }

    @Test
    public void testArithmeticMean() {
        assertBigDecimal(BigDecimal.valueOf(6/3), Statistics.arithmeticMean(toBigDecimals(1, 2, 3), MC));
    }

    @Test
    public void testMedianSorted() {
        assertBigDecimal(BigDecimal.valueOf(4), Statistics.medianSorted(toBigDecimals(1, 2, 3, 4, 4, 4, 4), MC));
        assertBigDecimal(BigDecimal.valueOf(1), Statistics.medianSorted(toBigDecimals(1, 1, 1, 2), MC));

        assertBigDecimal(BigDecimal.valueOf(3.5), Statistics.medianSorted(toBigDecimals(1, 2, 3, 4, 4, 4), MC));
        assertBigDecimal(BigDecimal.valueOf(1.5), Statistics.medianSorted(toBigDecimals(1, 1, 2, 4), MC));
    }

    @Test
    public void testMedianUnsorted() {
        assertBigDecimal(BigDecimal.valueOf(4), Statistics.medianUnsorted(toBigDecimals(4, 2, 3, 4, 1, 4, 4), MC));
        assertBigDecimal(BigDecimal.valueOf(1), Statistics.medianUnsorted(toBigDecimals(1, 2, 1, 1), MC));

        assertBigDecimal(BigDecimal.valueOf(3.5), Statistics.medianUnsorted(toBigDecimals(1, 4, 3, 2, 4, 4), MC));
        assertBigDecimal(BigDecimal.valueOf(1.5), Statistics.medianUnsorted(toBigDecimals(2, 1, 4, 1), MC));
    }

    @Test
    public void testSampleStandardDeviation() {
        // https://www.wolframalpha.com/input/?i=sample+standard+deviation+of+1,2,3
        assertBigDecimal(
                BigDecimal.valueOf(1),
                Statistics.sampleStandardDeviation(toBigDecimals(1, 2, 3), MC).round(new MathContext(5)));

        // https://www.wolframalpha.com/input/?i=sample+standard+deviation+of+1,2,3,4
        assertBigDecimal(
                BigDecimal.valueOf(1.2910),
                Statistics.sampleStandardDeviation(toBigDecimals(1, 2, 3, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationStandardDeviation() {
        // https://www.wolframalpha.com/input/?i=population+standard+deviation+of+1,2,3
        assertBigDecimal(
                BigDecimal.valueOf(0.81650),
                Statistics.populationStandardDeviation(toBigDecimals(1, 2, 3), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationVariance() {
        // https://www.wolframalpha.com/input/?i=variance+of+1,2,3,4&assumption=%7B%22C%22,+%22variance%22%7D+-%3E+%7B%22PopulationVariance%22%7D
        assertBigDecimal(
                BigDecimal.valueOf(0.66667),
                Statistics.populationVariance(toBigDecimals(1, 2, 3), MC).round(new MathContext(5)));
    }

    @Test
    public void testSampleVariance() {
        // https://www.wolframalpha.com/input/?i=sample+variance+of+1,2,3,4
        assertBigDecimal(
                BigDecimal.valueOf(1.6667),
                Statistics.sampleVariance(toBigDecimals(1, 2, 3, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationSkewness() {
        // https://www.wolframalpha.com/input/?i=Skewness%5B1,2,3,4,4%5D
        assertBigDecimal(
                BigDecimal.valueOf(-0.36317),
                Statistics.populationSkewness(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationKurtosis() {
        // https://www.wolframalpha.com/input/?i=kurtosis+1,2,3,4,4
        assertBigDecimal(
                BigDecimal.valueOf(1.6280),
                Statistics.populationKurtosis(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationExcessKurtosis() {
        // https://www.wolframalpha.com/input/?i=kurtosis+1,2,3,4,4
        assertBigDecimal(
                BigDecimal.valueOf(1.6280 - 3),
                Statistics.populationExcessKurtosis(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationSkewness2() {
        // https://www.wolframalpha.com/input/?i=Skewness%5B1,2,3,4,4%5D
        assertBigDecimal(
                BigDecimal.valueOf(-0.36317),
                Statistics.populationSkewness2(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationKurtosis2() {
        // https://www.wolframalpha.com/input/?i=kurtosis+1,2,3,4,4
        assertBigDecimal(
                BigDecimal.valueOf(1.6280),
                Statistics.populationKurtosis2(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testPopulationExcessKurtosis2() {
        // https://www.wolframalpha.com/input/?i=kurtosis+1,2,3,4,4
        assertBigDecimal(
                BigDecimal.valueOf(1.6280 - 3),
                Statistics.populationExcessKurtosis2(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    @Test
    public void testSampleSkewness() {
        // Excel: =SKEW(1;2;3;4;4)
        assertBigDecimal(
                BigDecimal.valueOf(-0.54139),
                Statistics.sampleSkewness(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }

    /*
    @Test
    public void testSampleKurtosis() {
        // Excel: =KURT(1;2;3;4;4)
        assertBigDecimal(
                BigDecimal.valueOf(-1.4879),
                Statistics.sampleKurtosis(toBigDecimals(1, 2, 3, 4, 4), MC).round(new MathContext(5)));
    }
    */

    @Test
    public void testHistogram() {
        Histogram histogram = Statistics.histogram(
                toBigDecimals(1, 2, 3, 5, 5, 5, 5, 6, 6, 7, 8, 8, 9),
                BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(2));

        assertEquals(5, histogram.size());

        assertEquals(BigDecimal.valueOf(0), histogram.getStart(0));
        assertEquals(BigDecimal.valueOf(2), histogram.getStart(1));
        assertEquals(BigDecimal.valueOf(4), histogram.getStart(2));
        assertEquals(BigDecimal.valueOf(6), histogram.getStart(3));
        assertEquals(BigDecimal.valueOf(8), histogram.getStart(4));

        assertEquals(BigDecimal.valueOf(2), histogram.getEnd(0));
        assertEquals(BigDecimal.valueOf(4), histogram.getEnd(1));
        assertEquals(BigDecimal.valueOf(6), histogram.getEnd(2));
        assertEquals(BigDecimal.valueOf(8), histogram.getEnd(3));
        assertEquals(BigDecimal.valueOf(10), histogram.getEnd(4));

        assertEquals(1, histogram.getCount(0));
        assertEquals(2, histogram.getCount(1));
        assertEquals(4, histogram.getCount(2));
        assertEquals(3, histogram.getCount(3));
        assertEquals(3, histogram.getCount(4));
    }

    @Test
    public void testCorrelation() {
        assertBigDecimal(
                BigDecimal.valueOf(1),
                Statistics.correlation(Arrays.asList(tuple(1, 1), tuple(2, 2), tuple(3, 3)), MC));

        assertBigDecimal(
                BigDecimal.valueOf(-1),
                Statistics.correlation(Arrays.asList(tuple(1, 3), tuple(2, 2), tuple(3, 1)), MC));

        assertBigDecimal(
                BigDecimal.valueOf(0.877),
                Statistics.correlation(Arrays.asList(tuple(15.5, 0.450), tuple(13.6, 0.420), tuple(13.5, 0.440), tuple(13.0, 0.395), tuple(13.3, 0.395), tuple(12.4, 0.370), tuple(11.1, 0.390), tuple(13.1, 0.400), tuple(16.1, 0.445), tuple(16.4, 0.470), tuple(13.4, 0.390), tuple(13.2, 0.400), tuple(14.3, 0.420), tuple(16.1, 0.450)), MC).round(new MathContext(3)));
    }

    @Test
    public void testCorrelation2() {
        assertBigDecimal(
                BigDecimal.valueOf(1.0),
                Statistics.correlation(
                        toBigDecimals(1, 2, 3),
                        toBigDecimals(1, 2, 3),
                        MC));

        assertBigDecimal(
                BigDecimal.valueOf(-1.0),
                Statistics.correlation(
                        toBigDecimals(1, 2, 3),
                        toBigDecimals(3, 2, 1),
                        MC));

        assertBigDecimal(
                BigDecimal.valueOf(0.877),
                Statistics.correlation(
                        toBigDecimals(15.5, 13.6, 13.5, 13.0, 13.3, 12.4, 11.1, 13.1, 16.1, 16.4, 13.4, 13.2, 14.3, 16.1),
                        toBigDecimals(0.450, 0.420, 0.440, 0.395, 0.395, 0.370, 0.390, 0.400, 0.445, 0.470, 0.390, 0.400, 0.420, 0.450),
                        MC).round(new MathContext(3)));
    }

    private static BigDecimal[] tuple(double... tuple) {
        return Arrays.stream(tuple)
                .mapToObj(v -> BigDecimal.valueOf(v))
                .collect(Collectors.toList()).toArray(new BigDecimal[tuple.length]);
    }

    private List<BigDecimal> toBigDecimals(double... values) {
        return Arrays.stream(values)
                .mapToObj(v -> BigDecimal.valueOf(v))
                .collect(Collectors.toList());
    }

    private static void assertBigDecimal(BigDecimal expected, BigDecimal actual) {
        if (expected.compareTo(actual) != 0) {
            fail(expected + " != " + actual);
        }
    }
}
