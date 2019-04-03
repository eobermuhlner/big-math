package ch.obermuhlner.math.big.statistics.old;

import java.math.BigDecimal;
import java.math.MathContext;

public class Correlation {
    private final MathContext mathContext;
    private final Statistics xValues;
    private final Statistics yValues;

    private BigDecimal covariance = null;

    public Correlation(MathContext mathContext) {
        this.mathContext = mathContext;
        xValues = new Statistics(mathContext);
        yValues = new Statistics(mathContext);
    }

    public synchronized void add(BigDecimal x, BigDecimal y) {
        xValues.add(x);
        yValues.add(y);

        covariance = null;
    }

    public synchronized int getCount() {
        return xValues.getCount();
    }

    public synchronized BigDecimal getCovariance() {
        if (covariance == null) {
            BigDecimal meanX = xValues.getArithmeticMean();
            BigDecimal meanY = yValues.getArithmeticMean();

            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i < xValues.getCount(); i++) {
                BigDecimal v = xValues.getValue(i).subtract(meanX, mathContext).multiply(yValues.getValue(i).subtract(meanY, mathContext), mathContext);
                total = total.add(v, mathContext);
            }

            covariance = total.divide(BigDecimal.valueOf(getCount()), mathContext);
        }

        return covariance;
    }

    public synchronized BigDecimal getPearsonsPopulationCorrelationCoefficient() {
        BigDecimal sX = xValues.getSampleStandardDeviation();
        BigDecimal sY = yValues.getSampleStandardDeviation();

        return getCovariance().divide(sX.multiply(sY, mathContext), mathContext);
    }

    public synchronized BigDecimal getAnotherCovariance() {
        if (covariance == null) {
            BigDecimal meanX = xValues.getArithmeticMean();
            BigDecimal meanY = yValues.getArithmeticMean();

            BigDecimal total = BigDecimal.ZERO;

            for (int i = 0; i < xValues.getCount(); i++) {
                BigDecimal v = xValues.getValue(i).subtract(meanX, mathContext).multiply(yValues.getValue(i).subtract(meanY, mathContext), mathContext);
                total = total.add(v, mathContext);
            }

            covariance = total;
        }

        return covariance;
    }

}
