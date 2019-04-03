package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class PopulationVarianceCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final ArithmeticMeanCalculator arithmeticMeanCalculator;

    private BigDecimal sumSquares = BigDecimal.ZERO;

    public PopulationVarianceCalculator(MathContext mathContext) {
        this.mathContext = mathContext;

        this.arithmeticMeanCalculator = new ArithmeticMeanCalculator(mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        arithmeticMeanCalculator.add(value);

        BigDecimal valueSquared = value.multiply(value, mathContext);
        sumSquares = sumSquares.add(valueSquared, mathContext);
    }

    public void combine(PopulationVarianceCalculator other) {
        arithmeticMeanCalculator.combine(other.arithmeticMeanCalculator);

        sumSquares = sumSquares.add(other.sumSquares, mathContext);
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal mean = arithmeticMeanCalculator.getResult();
        return sumSquares.divide(BigDecimal.valueOf(arithmeticMeanCalculator.getCount()), mathContext).subtract(mean.multiply(mean, mathContext));
    }

    public int getCount() {
        return arithmeticMeanCalculator.getCount();
    }
}
