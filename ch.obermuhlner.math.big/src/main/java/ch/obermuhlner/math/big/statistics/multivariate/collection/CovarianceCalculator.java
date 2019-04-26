package ch.obermuhlner.math.big.statistics.multivariate.collection;

import ch.obermuhlner.math.big.statistics.Statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Iterator;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class CovarianceCalculator implements MultivariateCollectionsCalculator<Collection<BigDecimal>, BigDecimal> {

    private final MathContext mathContext;
    private final int xValueIndex;
    private final int yValueIndex;

    public CovarianceCalculator(MathContext mathContext) {
        this(mathContext, 0, 1);
    }

    public CovarianceCalculator(MathContext mathContext, int xValueIndex, int yValueIndex) {
        this.mathContext = mathContext;
        this.xValueIndex = xValueIndex;
        this.yValueIndex = yValueIndex;
    }

    @Override
    public BigDecimal getResult(Collection<BigDecimal>... values) {
        BigDecimal xMean = Statistics.arithmeticMean(values[xValueIndex], mathContext);
        BigDecimal yMean = Statistics.arithmeticMean(values[yValueIndex], mathContext);

        Iterator<BigDecimal> xIterator = values[xValueIndex].iterator();
        Iterator<BigDecimal> yIterator = values[xValueIndex].iterator();

        int n = 0;
        BigDecimal sumTerms = ZERO;
        while (xIterator.hasNext() && yIterator.hasNext()) {
            BigDecimal x = xIterator.next();
            BigDecimal y = yIterator.next();

            BigDecimal term = x.subtract(xMean, mathContext).multiply(y.subtract(yMean, mathContext), mathContext);
            sumTerms = sumTerms.add(term, mathContext);
            n++;
        }

        return sumTerms.divide(valueOf(n), mathContext);
    }
}
