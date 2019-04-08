package ch.obermuhlner.math.big.statistics.univariate.collection;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class MedianCalculator implements UnivariateCollectionCalculator<List<BigDecimal>, BigDecimal> {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private final MathContext mathContext;

    public MedianCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public boolean needsSorted() {
        return true;
    }

    @Override
    public BigDecimal getResult(List<BigDecimal> sortedValues) {
        int n = sortedValues.size();
        if (n % 2 == 0) {
            return sortedValues.get(n / 2).add(sortedValues.get(n / 2 - 1), mathContext).divide(TWO, mathContext);
        } else {
            return sortedValues.get(n / 2);
        }
    }
}
