package ch.obermuhlner.math.big.statistics.univariate.list;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class SampleKurtosisCalculator implements UnivariateListCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final PopulationKurtosisCalculator populationKurtosisCalculator;

    public SampleKurtosisCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
        this.populationKurtosisCalculator = new PopulationKurtosisCalculator(mathContext);
    }

    @Override
    public BigDecimal getResult(List<BigDecimal> values) {
        int count = values.size();
        BigDecimal nMinus1 = BigDecimal.valueOf(count - 1);
        BigDecimal nMinus2 = BigDecimal.valueOf(count - 2);
        BigDecimal nMinus3 = BigDecimal.valueOf(count - 3);
        BigDecimal nPlus1 = BigDecimal.valueOf(count + 1);

        BigDecimal correction = nPlus1.multiply(nMinus1, mathContext).divide(nMinus2.multiply(nMinus3, mathContext), mathContext);
        BigDecimal kurtosis = populationKurtosisCalculator.getResult(values);

        return correction.multiply(kurtosis, mathContext);
    }
}
