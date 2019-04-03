package ch.obermuhlner.math.big.statistics.univariate.stream;

import ch.obermuhlner.math.big.statistics.univariate.Histogram;

import java.math.BigDecimal;
import java.util.Arrays;

public class HistogramCalculator implements UnivariateStreamCalculator<Histogram> {

    private final BigDecimal start;
    private final BigDecimal step;
    private final int count[];

    public HistogramCalculator(BigDecimal start, BigDecimal end, BigDecimal step) {
        this.start = start;
        this.step = step;
        int size = end.subtract(start).divide(step).intValue();

        this.count = new int[size];
    }

    @Override
    public void add(BigDecimal value) {
        int index = value.subtract(start).divide(step).intValue();
        if(index >= 0 && index < count.length) {
            count[index]++;
        }
    }

    public void combine(HistogramCalculator other) {
        if (!start.equals(other.start) || !step.equals(other.step) || count.length!=other.count.length) {
            throw new IllegalArgumentException("Can only combine equivalent HistogramCalculator");
        }

        for (int i = 0; i < count.length; i++) {
            count[i] += other.count[i];
        }
    }

    @Override
    public Histogram getResult() {
        return new Histogram(start, step, Arrays.copyOf(count, count.length));
    }
}
