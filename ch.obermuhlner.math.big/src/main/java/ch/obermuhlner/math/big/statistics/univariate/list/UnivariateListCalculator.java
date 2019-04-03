package ch.obermuhlner.math.big.statistics.univariate.list;

import java.math.BigDecimal;
import java.util.List;

public interface UnivariateListCalculator<R> {

    default boolean needsSorted() {
        return false;
    }

    R getResult(List<BigDecimal> values);
}
