package ch.obermuhlner.math.big.statistics.univariate.collection;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

public class SampleSkewnessCalculator implements UnivariateCollectionCalculator<Collection<BigDecimal>, BigDecimal> {

    private final MathContext mathContext;
    private final PopulationSkewnessCalculator populationSkewnessCalculator;

    public SampleSkewnessCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.populationSkewnessCalculator = new PopulationSkewnessCalculator(mathContext);
    }

    @Override
    public BigDecimal getResult(Collection<BigDecimal> values) {
        int count = values.size();
        BigDecimal n = BigDecimal.valueOf(count);
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);

        BigDecimal correction = BigDecimalMath.sqrt(n.multiply(nMinus1, mathContext), mathContext).divide(nMinus2, mathContext);

        return correction.multiply(populationSkewnessCalculator.getResult(values), mathContext);
    }
}
