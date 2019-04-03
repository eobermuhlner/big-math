package ch.obermuhlner.math.big.statistics.old;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

public class SimpleStatistics {
    protected final MathContext mathContext;

    private int count;
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal product = BigDecimal.ONE;
    private BigDecimal sumReciprocals = BigDecimal.ZERO;
    private BigDecimal min = null;
    private BigDecimal max = null;

    public SimpleStatistics(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public MathContext getMathContext() {
        return mathContext;
    }

    public synchronized void add(BigDecimal value) {
        count++;

        sum = sum.add(value, mathContext);
        product = product.multiply(value, mathContext);
        sumReciprocals = sumReciprocals.add(BigDecimalMath.reciprocal(value, mathContext));

        if (max == null || value.compareTo(max) > 0) {
            max = value;
        }
        if (min == null || value.compareTo(min) > 0) {
            min = value;
        }
    }

    public void add(BigDecimal... values) {
        for (int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    public void add(Collection<BigDecimal> values) {
        for (BigDecimal value: values) {
            add(value);
        }
    }

    public void add(double value) {
        add(BigDecimal.valueOf(value));
    }

    public void add(double... values) {
        for (int i = 0; i < values.length; i++) {
            add(BigDecimal.valueOf(values[i]));
        }
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getProduct() {
        return product;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public synchronized BigDecimal getArithmeticMean() {
        return sum.divide(BigDecimal.valueOf(count), mathContext);
    }

    public synchronized BigDecimal getGeometricMean() {
        return BigDecimalMath.root(product, BigDecimal.valueOf(count), mathContext);
    }

    public synchronized BigDecimal getHarmonicMean() {
        return BigDecimal.valueOf(count).divide(sumReciprocals, mathContext);
    }
}
