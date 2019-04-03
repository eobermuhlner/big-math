package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;
import java.math.MathContext;

public class ProductCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private BigDecimal product = BigDecimal.ONE;

    public ProductCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public void combine(ProductCalculator other) {
        product = product.multiply(other.product, mathContext);
    }

    @Override
    public void add(BigDecimal value) {
        product = product.multiply(value, mathContext);
    }

    @Override
    public BigDecimal getResult() {
        return product;
    }
}
