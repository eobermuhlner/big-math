package ch.obermuhlner.math.big.statistics.multivariate.list;

import ch.obermuhlner.math.big.statistics.multivariate.stream.MultivariateStreamCalculator;
import ch.obermuhlner.math.big.statistics.univariate.list.UnivariateListCalculator;
import ch.obermuhlner.math.big.statistics.univariate.stream.UnivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.List;

public class MultivariateStreamAsListCalculator<R> implements MultivariateListCalculator<R> {

    private final MultivariateStreamCalculator<R> calculator;

    public MultivariateStreamAsListCalculator(MultivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(List<BigDecimal[]> values) {
        for(BigDecimal[] tuple : values) {
            calculator.add(tuple);
        }
        return calculator.getResult();
    }
}
