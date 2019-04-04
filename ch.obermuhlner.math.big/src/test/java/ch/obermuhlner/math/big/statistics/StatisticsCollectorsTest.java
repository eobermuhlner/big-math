package ch.obermuhlner.math.big.statistics;

import ch.obermuhlner.math.big.statistics.univariate.Histogram;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.BigDecimalUtil.assertBigDecimal;
import static ch.obermuhlner.math.big.BigDecimalUtil.tuple;
import static org.junit.Assert.assertEquals;

public class StatisticsCollectorsTest {

    private static final MathContext MC = MathContext.DECIMAL64;

    @Test
    public void testMin() {
        BigDecimal result = Stream.of(4, 5, 1, 2, 3)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.min());
        assertBigDecimal(BigDecimal.valueOf(1), result);
    }

    @Test
    public void testMax() {
        BigDecimal result = Stream.of(4, 5, 1, 2, 3)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.max());
        assertBigDecimal(BigDecimal.valueOf(5), result);
    }

    @Test
    public void testSum() {
        BigDecimal result = Stream.of(0, 1, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.sum(MC));
        assertBigDecimal(BigDecimal.valueOf(6), result);
    }

    @Test
    public void testProduct() {
        BigDecimal result = Stream.of(1, 2, 3, 4)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.product(MC));
        assertBigDecimal(BigDecimal.valueOf(1*2*3*4), result);
    }

    @Test
    public void testArithmeticMean() {
        BigDecimal result = Stream.of(1, 2, 3)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.arithmeticMean(MC));
        assertBigDecimal(BigDecimal.valueOf(6/3), result);
    }

    @Test
    public void testMedian() {
        BigDecimal result = Stream.of(1, 2, 3, 4, 2, 2)
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.median(MC));
        assertBigDecimal(BigDecimal.valueOf(2), result);
    }

    @Test
    public void testHistogram() {
        Histogram histogram = Stream.of(1, 2, 3, 5, 5, 5, 5, 6, 6, 7, 8, 8, 9)
                .parallel()
                .map(x -> BigDecimal.valueOf(x))
                .collect(StatisticsCollectors.histogram(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(2)));

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
                Stream.of(tuple(1, 1), tuple(2, 2), tuple(3, 3))
                    .parallel()
                    .collect(StatisticsCollectors.correlation(MC)));

        assertBigDecimal(
                BigDecimal.valueOf(-1),
                Stream.of(tuple(1, 3), tuple(2, 2), tuple(3, 1))
                        .parallel()
                        .collect(StatisticsCollectors.correlation(MC)));

        // http://www.statstutor.ac.uk/resources/uploaded/pearsons.pdf
        assertBigDecimal(
                BigDecimal.valueOf(0.877),
                Stream.of(tuple(15.5, 0.450), tuple(13.6, 0.420), tuple(13.5, 0.440), tuple(13.0, 0.395), tuple(13.3, 0.395), tuple(12.4, 0.370), tuple(11.1, 0.390), tuple(13.1, 0.400), tuple(16.1, 0.445), tuple(16.4, 0.470), tuple(13.4, 0.390), tuple(13.2, 0.400), tuple(14.3, 0.420), tuple(16.1, 0.450))
                        .parallel()
                        .collect(StatisticsCollectors.correlation(MC)).round(new MathContext(3)));
    }
}
