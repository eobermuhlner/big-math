package ch.obermuhlner.math.big.statistics.univariate.stream;

import java.math.BigDecimal;

public interface UnivariateStreamCalculator<R> {

    default boolean needsSorted() {
        return false;
    }

    void add(BigDecimal value);

    R getResult();
}
