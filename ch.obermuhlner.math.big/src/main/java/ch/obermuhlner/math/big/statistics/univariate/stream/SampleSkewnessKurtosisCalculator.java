package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

// https://www.macroption.com/kurtosis-formula/
public class SampleSkewnessKurtosisCalculator implements UnivariateStreamCalculator<SkewnessKurtosis> {

    private final MathContext mathContext;
    private final PopulationSkewnessKurtosisCalculator populationSkewnessKurtosisCalculator;

    public SampleSkewnessKurtosisCalculator(MathContext mathContext) {
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
        int count = populationSkewnessKurtosisCalculator.getCount();
        BigDecimal n = BigDecimal.valueOf(count);
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);

        BigDecimal correction = BigDecimalMath.sqrt(n.multiply(nMinus1, mathContext), mathContext).divide(nMinus2, mathContext);
        BigDecimal skewness = populationSkewnessKurtosisCalculator.getSkewness();

        return correction.multiply(skewness, mathContext);
    }

    public BigDecimal getKurtosis() {
        // https://brownmath.com/stat/shape.htm
        int count = populationSkewnessKurtosisCalculator.getCount();
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);
        BigDecimal nMinus3 = BigDecimal.valueOf(count - 3);
        BigDecimal nPlus1 = BigDecimal.valueOf(count + 1);

        BigDecimal correction = nPlus1.multiply(nMinus1, mathContext).divide(nMinus2.multiply(nMinus3, mathContext), mathContext);
        BigDecimal kurtosis = populationSkewnessKurtosisCalculator.getKurtosis();

        return correction.multiply(kurtosis, mathContext);
    }

    @Override
    public SkewnessKurtosis getResult() {
        return new SkewnessKurtosis(getSkewness(), getKurtosis());
    }
}
