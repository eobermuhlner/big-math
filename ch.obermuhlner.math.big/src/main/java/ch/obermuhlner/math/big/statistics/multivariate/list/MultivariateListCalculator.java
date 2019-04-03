package ch.obermuhlner.math.big.statistics.multivariate.list;

import java.math.BigDecimal;
import java.util.List;

public interface MultivariateListCalculator<R> {

    R getResult(List<BigDecimal[]> values);
}
