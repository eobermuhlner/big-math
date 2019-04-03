package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class ArithmeticMeanCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private BigDecimal sum = BigDecimal.ZERO;
    private int count = 0;

    public ArithmeticMeanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public void combine(ArithmeticMeanCalculator other) {
        sum = sum.add(other.sum, mathContext);
        count += other.count;
    }

    @Override
    public void add(BigDecimal value) {
        sum = sum.add(value, mathContext);
        count++;
    }

    @Override
    public BigDecimal getResult() {
        return sum.divide(BigDecimal.valueOf(count), mathContext);
    }

    public int getCount() {
        return count;
    }
}
