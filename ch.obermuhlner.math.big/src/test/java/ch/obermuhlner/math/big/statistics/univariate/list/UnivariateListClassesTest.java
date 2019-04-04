package ch.obermuhlner.math.big.statistics.univariate.list;

import ch.obermuhlner.math.big.statistics.Statistics;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static ch.obermuhlner.math.big.BigDecimalUtil.assertBigDecimal;
import static ch.obermuhlner.math.big.BigDecimalUtil.toBigDecimals;


public class UnivariateListClassesTest {

    private static final MathContext MC = MathContext.DECIMAL64;

    @Test
    public void testPopulationSkewnessCalculator() {
        // https://www.wolframalpha.com/input/?i=Skewness%5B1,2,3,4,4%5D
        PopulationSkewnessCalculator calculator = new PopulationSkewnessCalculator(MC);
        assertBigDecimal(
                BigDecimal.valueOf(-0.36317),
                calculator.getResult(toBigDecimals(1, 2, 3, 4, 4)).round(new MathContext(5)));
    }

    @Test
    public void testPopulationKurtosisCalculator() {
        // https://www.wolframalpha.com/input/?i=kurtosis+1,2,3,4,4
        PopulationKurtosisCalculator calculator = new PopulationKurtosisCalculator(MC);
        assertBigDecimal(
                BigDecimal.valueOf(1.6280),
                calculator.getResult(toBigDecimals(1, 2, 3, 4, 4)).round(new MathContext(5)));
    }

    @Test
    public void testSampleSkewnessCalculator() {
        // Excel: =SKEW(1;2;3;4;4)
        SampleSkewnessCalculator calculator = new SampleSkewnessCalculator(MC);
        assertBigDecimal(
                BigDecimal.valueOf(-0.54139),
                calculator.getResult(toBigDecimals(1, 2, 3, 4, 4)).round(new MathContext(5)));
    }

    @Test
    public void testSampleExcessKurtosisCalculator() {
        // Excel: =KURT(1;2;3;4;4)
        SampleExcessKurtosisCalculator calculator = new SampleExcessKurtosisCalculator(MC);
        assertBigDecimal(
                BigDecimal.valueOf(-1.4879),
                calculator.getResult(toBigDecimals(1, 2, 3, 4, 4)).round(new MathContext(5)));
    }

}
