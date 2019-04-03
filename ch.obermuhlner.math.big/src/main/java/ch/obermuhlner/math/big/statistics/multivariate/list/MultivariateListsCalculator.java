package ch.obermuhlner.math.big.statistics.multivariate.list;

import java.math.BigDecimal;
import java.util.List;

public interface MultivariateListsCalculator<R> {

    R getResult(List<BigDecimal>... values);
}
