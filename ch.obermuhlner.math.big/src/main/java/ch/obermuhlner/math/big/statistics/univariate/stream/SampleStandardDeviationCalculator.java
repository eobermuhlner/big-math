package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class SampleStandardDeviationCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final SampleVarianceCalculator sampleVarianceCalculator;

    public SampleStandardDeviationCalculator(MathContext mathContext) {
        this.mathContext = mathContext;

        this.sampleVarianceCalculator = new SampleVarianceCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        sampleVarianceCalculator.add(value);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal populationVariance = sampleVarianceCalculator.getResult();
        return BigDecimalMath.sqrt(populationVariance, mathContext);
    }
}
