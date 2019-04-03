package ch.obermuhlner.math.big.statistics.multivariate.stream;

import java.math.BigDecimal;

public interface MultivariateStreamCalculator<R> {

    void add(BigDecimal... tuple);

    R getResult();
}
