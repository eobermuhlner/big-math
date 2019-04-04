package ch.obermuhlner.math.big.statistics.univariate.list;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class SampleExcessKurtosisCalculator implements UnivariateListCalculator<BigDecimal> {

    private static final BigDecimal B3 = BigDecimal.valueOf(3);

    private final MathContext mathContext;
    private final SampleKurtosisCalculator sampleKurtosisCalculator;

    public SampleExcessKurtosisCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.sampleKurtosisCalculator = new SampleKurtosisCalculator(mathContext);
    }

    @Override
    public BigDecimal getResult(List<BigDecimal> values) {
        int count = values.size();
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);
        BigDecimal nMinus3 = BigDecimal.valueOf(count - 3);

        BigDecimal kurtosis = sampleKurtosisCalculator.getResult(values);
        BigDecimal deltaCorrection = B3.multiply(nMinus1, mathContext).multiply(nMinus1, mathContext).divide(nMinus2.multiply(nMinus3, mathContext), mathContext);

        return kurtosis.subtract(deltaCorrection, mathContext);
    }
}
