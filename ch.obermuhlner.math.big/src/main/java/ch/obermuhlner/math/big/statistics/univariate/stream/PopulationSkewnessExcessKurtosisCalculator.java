package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.statistics.type.SkewnessKurtosis;

import java.math.BigDecimal;
import java.math.MathContext;

// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
public class PopulationSkewnessExcessKurtosisCalculator implements UnivariateStreamCalculator<SkewnessKurtosis> {

    private static final BigDecimal B3 = BigDecimal.valueOf(3);

    private final MathContext mathContext;
    private final PopulationSkewnessKurtosisCalculator populationSkewnessKurtosisCalculator;

    private final boolean calculateSkewness;
    private final boolean calculateKurtosis;

    public PopulationSkewnessExcessKurtosisCalculator(MathContext mathContext) {
        this(mathContext, true, true);
    }

    public PopulationSkewnessExcessKurtosisCalculator(MathContext mathContext, boolean calculateSkewness, boolean calculateKurtosis) {
        this.mathContext = mathContext;
        this.populationSkewnessKurtosisCalculator = new PopulationSkewnessKurtosisCalculator(mathContext, calculateSkewness, calculateKurtosis);
        this.calculateSkewness = calculateSkewness;
        this.calculateKurtosis = calculateKurtosis;
    }

    @Override
    public void add(BigDecimal value) {
        populationSkewnessKurtosisCalculator.add(value);
    }

    public int getCount() {
        return populationSkewnessKurtosisCalculator.getCount();
    }

    public BigDecimal getSkewness() {
        if (calculateSkewness) {
            return populationSkewnessKurtosisCalculator.getSkewness();
        } else {
            return null;
        }
    }

    public BigDecimal getKurtosis() {
        if (calculateKurtosis) {
            return populationSkewnessKurtosisCalculator.getKurtosis().subtract(B3, mathContext);
        } else {
            return null;
        }
    }

    @Override
    public SkewnessKurtosis getResult() {
        return new SkewnessKurtosis(getSkewness(), getKurtosis());
    }

}