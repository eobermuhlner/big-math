package ch.obermuhlner.math.big.example.internal.statistics;

import ch.obermuhlner.math.big.statistics.univariate.collection.UnivariateCollectionCalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

public class ArithmeticMeanCalculator implements UnivariateCollectionCalculator<Collection<BigDecimal>, BigDecimal> {

    private final MathContext mathContext;

    public ArithmeticMeanCalculator(MathContext mathContext) {
        this.mathContext = mathContext;
    }

    @Override
    public BigDecimal getResult(Collection<BigDecimal> values) {
        int count = values.size();

        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal value : values) {
            sum = sum.add(value, mathContext);
        }

        return sum.divide(BigDecimal.valueOf(count), mathContext);
    }
}
