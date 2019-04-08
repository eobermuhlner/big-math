package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.statistics.type.SkewnessKurtosis;

import java.math.BigDecimal;
import java.math.MathContext;

// https://www.macroption.com/kurtosis-formula/
public class SampleSkewnessExcessKurtosisCalculator implements UnivariateStreamCalculator<SkewnessKurtosis> {

    private static final BigDecimal B3 = BigDecimal.valueOf(3);

    private final MathContext mathContext;
    private final SampleSkewnessKurtosisCalculator sampleSkewnessKurtosisCalculator;

    private final boolean calculateSkewness;
    private final boolean calculateKurtosis;

    public SampleSkewnessExcessKurtosisCalculator(MathContext mathContext) {
        this(mathContext, true, true);
    }

    public SampleSkewnessExcessKurtosisCalculator(MathContext mathContext, boolean calculateSkewness, boolean calculateKurtosis) {
        this.mathContext = mathContext;
        this.sampleSkewnessKurtosisCalculator = new SampleSkewnessKurtosisCalculator(mathContext, calculateSkewness, calculateKurtosis);
        this.calculateSkewness = calculateSkewness;
        this.calculateKurtosis = calculateKurtosis;
    }

    @Override
    public void add(BigDecimal value) {
        sampleSkewnessKurtosisCalculator.add(value);
    }

    public BigDecimal getSkewness() {
        if (calculateSkewness) {
            return sampleSkewnessKurtosisCalculator.getSkewness();
        } else {
            return null;
        }
    }

    public BigDecimal getKurtosis() {
        if (calculateKurtosis) {
            int count = sampleSkewnessKurtosisCalculator.getCount();
            BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
            BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);
            BigDecimal nMinus3 = BigDecimal.valueOf(count - 3);

            BigDecimal kurtosis = sampleSkewnessKurtosisCalculator.getKurtosis();
            BigDecimal deltaCorrection = B3.multiply(nMinus1, mathContext).multiply(nMinus1, mathContext).divide(nMinus2.multiply(nMinus3, mathContext), mathContext);

            return kurtosis.subtract(deltaCorrection, mathContext);
        } else {
            return null;
        }
    }

    @Override
    public SkewnessKurtosis getResult() {
        return new SkewnessKurtosis(getSkewness(), getKurtosis());
    }
}
