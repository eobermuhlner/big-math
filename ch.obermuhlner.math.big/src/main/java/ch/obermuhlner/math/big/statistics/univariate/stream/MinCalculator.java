package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class MinCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private BigDecimal min = null;

    @Override
    public void add(BigDecimal value) {
        if (min == null || value.compareTo(min) < 0) {
            min = value;
        }
    }

    public void combine(MinCalculator other) {
        if (min == null) {
            min = other.min;
        } else {
            if (other.min != null && other.min.compareTo(min) < 0) {
                min = other.min;
            }
        }
    }

    @Override
    public BigDecimal getResult() {
        return min;
    }
}
