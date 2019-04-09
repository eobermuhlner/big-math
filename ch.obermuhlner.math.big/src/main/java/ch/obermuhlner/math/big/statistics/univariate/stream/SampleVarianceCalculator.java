package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class SampleVarianceCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final PopulationVarianceCalculator populationVarianceCalculator;

    public SampleVarianceCalculator(MathContext mathContext) {
        this.mathContext = mathContext;

        this.populationVarianceCalculator = new PopulationVarianceCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        populationVarianceCalculator.add(value);
    }

    public void combine(SampleVarianceCalculator other) {
        populationVarianceCalculator.combine(other.populationVarianceCalculator);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal populationVariance = populationVarianceCalculator.getResult();
        int n = populationVarianceCalculator.getCount();

        return populationVariance.multiply(BigDecimal.valueOf(n), mathContext).divide(BigDecimal.valueOf(n - 1), mathContext);
    }
}
