package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.statistics.univariate.list.UnivariateListCalculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnivariateWindowingStreamCalculator<R> implements UnivariateStreamCalculator<R> {

    private final UnivariateListCalculator<R> calculator;
    private int windowSize;

    private final List<BigDecimal> values = new ArrayList<>();

    private BigDecimal lastValue = null;
    private boolean valuesSorted = true;

    public UnivariateWindowingStreamCalculator(UnivariateListCalculator<R> calculator, int windowSize) {
        this.calculator = calculator;
        this.windowSize = windowSize;
    }

    @Override
    public boolean needsSorted() {
        return calculator.needsSorted();
    }

    @Override
    public void add(BigDecimal value) {
        values.add(value);
        if (values.size() > windowSize) {
            values.remove(0);
        }

        if (valuesSorted) {
            if (lastValue != null && value.compareTo(lastValue) < 0) {
                valuesSorted = false;
            }
            lastValue = value;
        }
    }

    @Override
    public R getResult() {
        List<BigDecimal> sortedValues = values;
        if (calculator.needsSorted() && !valuesSorted) {
            Collections.sort(sortedValues);
        }
        return calculator.getResult(sortedValues);
    }
}
