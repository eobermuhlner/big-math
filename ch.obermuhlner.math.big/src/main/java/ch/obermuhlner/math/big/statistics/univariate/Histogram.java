package ch.obermuhlner.math.big.statistics.univariate;

import java.math.BigDecimal;

public class Histogram {

    private final BigDecimal start;
    private final BigDecimal step;
    private final int[] count;

    public Histogram(BigDecimal start, BigDecimal step, int[] count) {
        this.start = start;
        this.step = step;
        this.count = count;
    }

    public int size() {
        return count.length;
    }

    public int getCount(int index) {
        return count[index];
    }

    public BigDecimal getStart(int index) {
        return start.add(step.multiply(BigDecimal.valueOf(index)));
    }

    public BigDecimal getEnd(int index) {
        return getStart(index).add(step);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Histogram(");
        for (int i = 0; i < size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(getStart(i));
            builder.append(":");
            builder.append(getCount(i));
        }
        builder.append(")");
        return builder.toString();
    }
}
