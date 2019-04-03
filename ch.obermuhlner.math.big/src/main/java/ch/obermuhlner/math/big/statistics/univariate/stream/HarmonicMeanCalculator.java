package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class HarmonicMeanCalculator implements UnivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;

    private BigDecimal sumReciprocals = BigDecimal.ZERO;
    private int count = 0;

    public HarmonicMeanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    public void combine(HarmonicMeanCalculator other) {
        sumReciprocals = sumReciprocals.add(other.sumReciprocals, mathContext);
        count += other.count;
    }

    @Override
    public void add(BigDecimal value) {
        sumReciprocals = sumReciprocals.add(BigDecimalMath.reciprocal(value, mathContext), mathContext);
        count++;
    }

    @Override
    public BigDecimal getResult() {
        return BigDecimal.valueOf(count).divide(sumReciprocals, mathContext);
    }

    public int getCount() {
        return count;
    }
}
