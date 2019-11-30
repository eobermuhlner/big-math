package ch.obermuhlner.math.big.vector;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;

public final class IndexValue {
    public final int index;
    public final BigDecimal value;

    private IndexValue(int index, BigDecimal value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexValue that = (IndexValue) o;
        return Objects.equals(index, that.index) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return index + ": " + value;
    }

    public static IndexValue coordValue(int index, double value) {
        return coordValue(index, valueOf(value));
    }

    public static IndexValue coordValue(int index, BigDecimal value) {
        return new IndexValue(index, value);
    }
}
