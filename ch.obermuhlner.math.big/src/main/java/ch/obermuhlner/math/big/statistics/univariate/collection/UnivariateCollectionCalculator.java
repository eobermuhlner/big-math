package ch.obermuhlner.math.big.statistics.univariate.collection;

import java.math.BigDecimal;
import java.util.Collection;

public interface UnivariateCollectionCalculator<C extends Collection<BigDecimal>, R> {

    default boolean needsSorted() {
        return false;
    }

    R getResult(C values);
}
