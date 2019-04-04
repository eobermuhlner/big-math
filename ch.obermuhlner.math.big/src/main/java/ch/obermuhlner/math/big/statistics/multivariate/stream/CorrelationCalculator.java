package ch.obermuhlner.math.big.statistics.multivariate.stream;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class CorrelationCalculator implements MultivariateStreamCalculator<BigDecimal> {

    private final MathContext mathContext;
    private final int xValueIndex;
    private final int yValueIndex;

    private int count = 0;
    private BigDecimal sumX = BigDecimal.ZERO;
    private BigDecimal sumY = BigDecimal.ZERO;
    private BigDecimal sumXY = BigDecimal.ZERO;
    private BigDecimal sumXX = BigDecimal.ZERO;
    private BigDecimal sumYY = BigDecimal.ZERO;

    public CorrelationCalculator(MathContext mathContext) {
        this(mathContext, 0, 1);
    }

    public CorrelationCalculator(MathContext mathContext, int xValueIndex, int yValueIndex) {
        this.mathContext = mathContext;
        this.xValueIndex = xValueIndex;
        this.yValueIndex = yValueIndex;
    }

    @Override
    public void add(BigDecimal... tuple) {
        BigDecimal x = tuple[xValueIndex];
        BigDecimal y = tuple[yValueIndex];

        sumX = sumX.add(x, mathContext);
        sumY = sumY.add(y, mathContext);
        sumXY = sumXY.add(x.multiply(y, mathContext), mathContext);
        sumXX = sumXX.add(x.multiply(x, mathContext), mathContext);
        sumYY = sumYY.add(y.multiply(y, mathContext), mathContext);
        count++;
    }

    public void combine(CorrelationCalculator other) {
        sumX = sumX.add(other.sumX, mathContext);
        sumY = sumY.add(other.sumY, mathContext);
        sumXY = sumXY.add(other.sumXY, mathContext);
        sumXX = sumXX.add(other.sumXX, mathContext);
        sumYY = sumYY.add(other.sumYY, mathContext);
        count += other.count;
    }

    @Override
    public BigDecimal getResult() {
        BigDecimal nom = BigDecimal.valueOf(count).multiply(sumXY, mathContext).subtract(sumX.multiply(sumY, mathContext), mathContext);
        BigDecimal denom1 = BigDecimalMath.sqrt(BigDecimal.valueOf(count).multiply(sumXX, mathContext).subtract(sumX.multiply(sumX, mathContext), mathContext), mathContext);
        BigDecimal denom2 = BigDecimalMath.sqrt(BigDecimal.valueOf(count).multiply(sumYY, mathContext).subtract(sumY.multiply(sumY, mathContext), mathContext), mathContext);
        return nom.divide(denom1.multiply(denom2, mathContext), mathContext);
    }
}
