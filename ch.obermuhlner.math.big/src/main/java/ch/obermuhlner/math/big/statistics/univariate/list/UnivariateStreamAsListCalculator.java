package ch.obermuhlner.math.big.statistics.univariate.list;

import ch.obermuhlner.math.big.statistics.univariate.stream.UnivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.List;

public class UnivariateStreamAsListCalculator<R> implements UnivariateListCalculator<R> {

    private final UnivariateStreamCalculator<R> calculator;

    public UnivariateStreamAsListCalculator(UnivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(List<BigDecimal> values) {
        for(BigDecimal value : values) {
            calculator.add(value);
        }
        return calculator.getResult();
    }
}
