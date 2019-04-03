package ch.obermuhlner.math.big.statistics.univariate.list;

import ch.obermuhlner.math.big.statistics.Statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

// Fisher-Pearson coefficient
// trivial implementation
// https://www.itl.nist.gov/div898/handbook/eda/section3/eda35b.htm
public class PopulationSkewnessCalculator implements UnivariateListCalculator<BigDecimal> {

    private MathContext mathContext;

    public PopulationSkewnessCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getResult(List<BigDecimal> values) {
        BigDecimal mean = Statistics.arithmeticMean(values, mathContext);
        BigDecimal s = Statistics.populationStandardDeviation(values, mathContext);

        BigDecimal nom = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            BigDecimal diff = value.subtract(mean, mathContext);
            BigDecimal cubic = diff.multiply(diff, mathContext).multiply(diff, mathContext);
            nom = nom.add(cubic);
        }

        BigDecimal denom = s.multiply(s, mathContext).multiply(s, mathContext);
        denom = denom.multiply(BigDecimal.valueOf(values.size()), mathContext);

        return nom.divide(denom, mathContext);
    }
}
