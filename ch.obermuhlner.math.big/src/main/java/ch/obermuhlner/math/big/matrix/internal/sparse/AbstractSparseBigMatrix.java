package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSparseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final Map<Integer, BigDecimal> data = new HashMap<>();
    protected BigDecimal defaultValue = BigDecimal.ZERO;

    public AbstractSparseBigMatrix(int rows, int columns, BigDecimal... values) {
        this.rows = rows;
        this.columns = columns;

        for (int i = 0; i < values.length; i++) {
            data.put(i, values[i]);
        }
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public BigDecimal get(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("row: " + row);
        }
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("column: " + columns);
        }

        int index = row*columns + column;
        return data.getOrDefault(index, defaultValue);
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("row: " + row);
        }
        if (column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("row: " + row);
        }

        int index = row*columns + column;
        if (value.equals(defaultValue)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }
}
