package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class GeometricMeanCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private BigDecimal product = BigDecimal.ONE;
    private int count = 0;

    public GeometricMeanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public void combine(GeometricMeanCalculator other) {
        product = product.multiply(other.product, mathContext);
        count += other.count;
    }

    @Override
    public void add(BigDecimal value) {
        product = product.multiply(value, mathContext);
        count++;
    }

    @Override
    public BigDecimal getResult() {
        return BigDecimalMath.root(product, BigDecimal.valueOf(count), mathContext);
    }

    public int getCount() {
        return count;
    }
}
