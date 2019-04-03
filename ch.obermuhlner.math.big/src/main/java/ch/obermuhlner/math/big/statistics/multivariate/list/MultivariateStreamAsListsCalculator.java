package ch.obermuhlner.math.big.statistics.multivariate.list;

import ch.obermuhlner.math.big.statistics.multivariate.stream.MultivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.List;

public class MultivariateStreamAsListsCalculator<R> implements MultivariateListsCalculator<R> {

    private final MultivariateStreamCalculator<R> calculator;

    public MultivariateStreamAsListsCalculator(MultivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(List<BigDecimal>... values) {
        for (int i = 0; i < values[0].size(); i++) {
            BigDecimal[] tuple = new BigDecimal[values.length];
            for (int j = 0; j < values.length; j++) {
                tuple[j] = values[j].get(i);
            }
            calculator.add(tuple);
        }
        return calculator.getResult();
    }
}
