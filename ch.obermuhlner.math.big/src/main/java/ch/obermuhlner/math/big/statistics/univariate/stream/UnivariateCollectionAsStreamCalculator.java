package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.statistics.univariate.collection.UnivariateCollectionCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UnivariateCollectionAsStreamCalculator<C extends Collection<BigDecimal>, R> implements UnivariateStreamCalculator<R> {

    private final UnivariateCollectionCalculator<C, R> calculator;

    private final List<BigDecimal> values = new ArrayList<>();

    private BigDecimal lastValue = null;
    private boolean valuesSorted = true;

    public UnivariateCollectionAsStreamCalculator(UnivariateCollectionCalculator<C, R> calculator) {
        this.calculator = calculator;
    }

    @Override
    public boolean needsSorted() {
        return calculator.needsSorted();
    }

    @Override
    public void add(BigDecimal value) {
        values.add(value);

        if (valuesSorted) {
            if (lastValue != null && value.compareTo(lastValue) < 0) {
                valuesSorted = false;
            }
            lastValue = value;
        }
    }

    public void combine(UnivariateCollectionAsStreamCalculator<C, R> other) {
        if (valuesSorted && other.valuesSorted) {
            // TODO use merge sort if both are sorted
            values.addAll(other.values);
            valuesSorted = false;
        } else {
            values.addAll(other.values);
            valuesSorted = false;
        }
    }

    @Override
    public R getResult() {
        if (calculator.needsSorted() && !valuesSorted) {
            Collections.sort(values);
        }
        return calculator.getResult((C) values);
    }
}
