package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
public class PopulationSkewnessExcessKurtosisCalculator implements UnivariateStreamCalculator<SkewnessKurtosis> {

    private static final BigDecimal B3 = BigDecimal.valueOf(3);

    private final MathContext mathContext;
    private final PopulationSkewnessKurtosisCalculator populationSkewnessKurtosisCalculator;

    public PopulationSkewnessExcessKurtosisCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.populationSkewnessKurtosisCalculator = new PopulationSkewnessKurtosisCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        populationSkewnessKurtosisCalculator.add(value);
    }

    public int getCount() {
        return populationSkewnessKurtosisCalculator.getCount();
    }

    public BigDecimal getSkewness() {
        return populationSkewnessKurtosisCalculator.getSkewness();
    }

    public BigDecimal getKurtosis() {
        return populationSkewnessKurtosisCalculator.getKurtosis().subtract(B3, mathContext);
    }

    @Override
    public SkewnessKurtosis getResult() {
        return new SkewnessKurtosis(getSkewness(), getKurtosis());
    }

}