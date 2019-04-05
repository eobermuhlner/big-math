package ch.obermuhlner.math.big.statistics.multivariate.collection;

import ch.obermuhlner.math.big.statistics.multivariate.stream.MultivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class MultivariateStreamAsCollectionCalculator<C extends Collection<BigDecimal[]>, R> implements MultivariateCollectionCalculator<C, R> {

    private final MultivariateStreamCalculator<R> calculator;

    public MultivariateStreamAsCollectionCalculator(MultivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(C tuples) {
        for(BigDecimal[] tuple : tuples) {
            calculator.add(tuple);
        }
        return calculator.getResult();
    }
}
