package ch.obermuhlner.math.big.statistics.univariate.collection;

import ch.obermuhlner.math.big.statistics.univariate.stream.UnivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.Collection;

public class UnivariateStreamAsCollectionCalculator<C extends Collection<BigDecimal>, R> implements UnivariateCollectionCalculator<C, R> {

    private final UnivariateStreamCalculator<R> calculator;

    public UnivariateStreamAsCollectionCalculator(UnivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(C values) {
        for(BigDecimal value : values) {
            calculator.add(value);
        }
        return calculator.getResult();
    }
}
