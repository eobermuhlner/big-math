package ch.obermuhlner.math.big.statistics.multivariate.collection;

import ch.obermuhlner.math.big.statistics.multivariate.stream.MultivariateStreamCalculator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

public class MultivariateStreamAsCollectionsCalculator<C extends Collection<BigDecimal>, R> implements MultivariateCollectionsCalculator<C, R> {

    private final MultivariateStreamCalculator<R> calculator;

    public MultivariateStreamAsCollectionsCalculator(MultivariateStreamCalculator<R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public R getResult(C... values) {
        int n = values.length;
        Iterator<BigDecimal>[] iterators = new Iterator[n];
        for (int i = 0; i < n; i++) {
            iterators[i] = values[i].iterator();
        }

        boolean finished = false;
        while (!finished) {
            finished = true;
            BigDecimal[] tuple = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                if (iterators[i].hasNext()) {
                    finished = false;
                    tuple[i] = iterators[i].next();
                }
            }
            if (!finished) {
                calculator.add(tuple);
            }
        }

        return calculator.getResult();
    }
}
