package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class CountCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private int count = 0;

    @Override
    public void add(BigDecimal value) {
        count++;
    }

    @Override
    public BigDecimal getResult() {
        return BigDecimal.valueOf(count);
    }
}
