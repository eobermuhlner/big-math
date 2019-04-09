package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class PopulationStandardDeviationCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final PopulationVarianceCalculator populationVarianceCalculator;

    public PopulationStandardDeviationCalculator(MathContext mathContext) {
        this.mathContext = mathContext;

        this.populationVarianceCalculator = new PopulationVarianceCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        populationVarianceCalculator.add(value);
    }

    public void combine(PopulationStandardDeviationCalculator other) {
        populationVarianceCalculator.combine(other.populationVarianceCalculator);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal populationVariance = populationVarianceCalculator.getResult();
        return BigDecimalMath.sqrt(populationVariance, mathContext);
    }
}
