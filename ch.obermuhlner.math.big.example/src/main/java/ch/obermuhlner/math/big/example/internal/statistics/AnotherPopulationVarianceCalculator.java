package ch.obermuhlner.math.big.example.internal.statistics;

import ch.obermuhlner.math.big.statistics.univariate.stream.UnivariateStreamCalculator;

import java.math.BigDecimal;
import java.math.MathContext;

// https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
public class AnotherPopulationVarianceCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private int count = 0;
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal sumSquares = BigDecimal.ZERO;

    public AnotherPopulationVarianceCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public void add(BigDecimal value) {
        count++;
        sum = sum.add(value, mathContext);
        BigDecimal valueSquared = value.multiply(value, mathContext);
        sumSquares = sumSquares.add(valueSquared, mathContext);
    }

    public void combine(AnotherPopulationVarianceCalculator other) {
        count = count + other.count;
        sum = sum.add(other.sum);
        sumSquares = sumSquares.add(other.sumSquares, mathContext);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal n = BigDecimal.valueOf(count);
        return sumSquares.subtract(sum.multiply(sum, mathContext).divide(n, mathContext), mathContext).divide(n, mathContext);
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getSum() {
        return sum;
    }
}
