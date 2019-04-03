package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class SumCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private BigDecimal sum = BigDecimal.ZERO;

    public SumCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public void combine(SumCalculator other) {
        sum = sum.add(other.sum, mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        sum = sum.add(value, mathContext);
    }

    @Override
    public BigDecimal getResult() {
        return sum;
    }
}
