package ch.obermuhlner.math.big.statistics.old;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistics extends SimpleStatistics {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private final List<BigDecimal> values = new ArrayList<>();
    private List<BigDecimal> sortedValues = null;

    public Statistics(MathContext mathContext) {
        super(mathContext);
    }

    @Override
    public synchronized void add(BigDecimal value) {
        super.add(value);

        values.add(value);
        sortedValues = null;
    }

    public synchronized BigDecimal getValue(int index) {
        return values.get(index);
    }

    public synchronized BigDecimal getSortedValue(int index) {
        ensureSortedValues();

        return sortedValues.get(index);
    }

    public synchronized BigDecimal getMedian() {
        ensureSortedValues();

        int n = sortedValues.size();
        if (n % 2 == 0) {
            return sortedValues.get(n / 2).add(sortedValues.get(n / 2 - 1)).divide(TWO);
        } else {
            return sortedValues.get(n / 2);
        }
    }

    private void ensureSortedValues() {
        if (sortedValues == null) {
            sortedValues = new ArrayList<>(values);
            Collections.sort(sortedValues);
        }
    }

    public BigDecimal getVariance() {
        return getVariance(0);
    }

    public BigDecimal getSampleVariance() {
        return getVariance(-1);
    }

    public BigDecimal getPopulationStandardDeviation() {
        return getPopulationStandardDeviation(0);
    }

    public BigDecimal getSampleStandardDeviation() {
        return getPopulationStandardDeviation(-1);
    }

    private BigDecimal getPopulationStandardDeviation(int countOffset) {
        return BigDecimalMath.sqrt(getVariance(countOffset), mathContext);
    }

    private BigDecimal getVariance(int countOffset) {
        BigDecimal sumSquareErrors = BigDecimal.ZERO;
        BigDecimal denom;

        synchronized (this) {
            denom = BigDecimal.valueOf(getCount() + countOffset);
            BigDecimal average = getArithmeticMean();

            for (BigDecimal value : values) {
                BigDecimal error = value.subtract(average, mathContext);
                BigDecimal squareError = error.multiply(error);
                sumSquareErrors = sumSquareErrors.add(squareError, mathContext);
            }
        }

        return sumSquareErrors.divide(denom, mathContext);
    }

    public BigDecimal getPopulationStandardScore(BigDecimal value) {
        return value.subtract(getArithmeticMean(), mathContext).divide(getPopulationStandardDeviation(), mathContext);
    }

    public BigDecimal getSampleStandardScore(BigDecimal value) {
        return value.subtract(getArithmeticMean(), mathContext).divide(getSampleStandardDeviation(), mathContext);
    }
}
