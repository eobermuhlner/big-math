package ch.obermuhlner.math.big.statistics.multivariate.collection;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface MultivariateCollectionCalculator<C extends Collection<BigDecimal[]>, R> {

    R getResult(C tuples);
}
