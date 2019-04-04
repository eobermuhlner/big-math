package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

// https://www.macroption.com/kurtosis-formula/
public class SampleSkewnessExcessKurtosisCalculator implements UnivariateStreamCalculator<SkewnessKurtosis> {

    private static final BigDecimal B3 = BigDecimal.valueOf(3);

    private final MathContext mathContext;
    private final SampleSkewnessKurtosisCalculator sampleSkewnessKurtosisCalculator;

    public SampleSkewnessExcessKurtosisCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.sampleSkewnessKurtosisCalculator = new SampleSkewnessKurtosisCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        sampleSkewnessKurtosisCalculator.add(value);
    }

    public BigDecimal getSkewness() {
        return sampleSkewnessKurtosisCalculator.getSkewness();
    }

    public BigDecimal getKurtosis() {
        int count = sampleSkewnessKurtosisCalculator.getCount();
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);
        BigDecimal nMinus3 = BigDecimal.valueOf(count - 3);

        BigDecimal kurtosis = sampleSkewnessKurtosisCalculator.getKurtosis();
        BigDecimal deltaCorrection = B3.multiply(nMinus1, mathContext).multiply(nMinus1, mathContext).divide(nMinus2.multiply(nMinus3, mathContext), mathContext);

        return kurtosis.subtract(deltaCorrection, mathContext);
    }

    @Override
    public SkewnessKurtosis getResult() {
        return new SkewnessKurtosis(getSkewness(), getKurtosis());
    }
}
