package ch.obermuhlner.math.big.statistics.univariate.collection;

import ch.obermuhlner.math.big.statistics.Statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

// Fisher-Pearson coefficient
// trivial implementation
// https://www.itl.nist.gov/div898/handbook/eda/section3/eda35b.htm
public class PopulationSkewnessCalculator implements UnivariateCollectionCalculator<Collection<BigDecimal>, BigDecimal> {

    private MathContext mathContext;

    public PopulationSkewnessCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getResult(Collection<BigDecimal> values) {
        BigDecimal n = BigDecimal.valueOf(values.size());
        BigDecimal mean = Statistics.arithmeticMean(values, mathContext);
        BigDecimal s = Statistics.populationStandardDeviation(values, mathContext);

        BigDecimal nom = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            BigDecimal diff = value.subtract(mean, mathContext);
            BigDecimal diffPow3 = diff.multiply(diff, mathContext).multiply(diff, mathContext);
            nom = nom.add(diffPow3);
        }

        BigDecimal sPow3 = s.multiply(s, mathContext).multiply(s, mathContext);
        BigDecimal denom = n.multiply(sPow3, mathContext);

        return nom.divide(denom, mathContext);
    }
}
