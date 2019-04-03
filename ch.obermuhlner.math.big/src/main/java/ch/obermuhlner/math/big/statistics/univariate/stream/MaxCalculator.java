package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;

public class MaxCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private BigDecimal max = null;

    @Override
    public void add(BigDecimal value) {
        if (max == null || value.compareTo(max) > 0) {
            max = value;
        }
    }

    public void combine(MaxCalculator other) {
        if (max == null) {
            max = other.max;
        } else {
            if (other.max != null && other.max.compareTo(max) > 0) {
                max = other.max;
            }
        }
    }

    @Override
    public BigDecimal getResult() {
        return max;
    }
}
