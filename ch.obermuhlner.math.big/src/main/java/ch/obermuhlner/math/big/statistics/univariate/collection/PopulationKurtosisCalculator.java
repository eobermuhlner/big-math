package ch.obermuhlner.math.big.statistics.univariate.collection;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.statistics.Statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

// Fisher-Pearson coefficient
// trivial implementation
// https://www.itl.nist.gov/div898/handbook/eda/section3/eda35b.htm
public class PopulationKurtosisCalculator implements UnivariateCollectionCalculator<Collection<BigDecimal>, BigDecimal> {

    private MathContext mathContext;

    public PopulationKurtosisCalculator(MathContext mathContext) {
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
            BigDecimal diffPow4 = BigDecimalMath.pow(diff, 4, mathContext);
            nom = nom.add(diffPow4);
        }

        BigDecimal sPow4 = BigDecimalMath.pow(s, 4, mathContext);
        BigDecimal denom = n.multiply(sPow4, mathContext);

        return nom.divide(denom, mathContext);
    }
}
